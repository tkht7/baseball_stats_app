package io.github.m_mememe.npb_stats_ref


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eclipsesource.json.Json
import com.eclipsesource.json.JsonArray
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch



class TeamFragment : Fragment() {
    // 画面遷移時の引数受け取り
    private val args: TeamFragmentArgs by navArgs()

    private val leagueId = MainFragment().leagueId
    private val leagueLogo = MainFragment().leagueLogo
    private val teamList = listOf<String>(
        "L",        //西武ライオンズ
        "H",        //ソフトバンク
        "E",       //楽天
        "M",      //千葉ロッテ
        "F",     //日本ハム
        "B",    //オリックス
        "G",       //ジャイアンツ
        "DB",     //DeNA
        "T",       //阪神
        "C",         //カープ
        "D",      //中日
        "S"      //ヤクルト
    )
    private val statsTypeList = listOf(
        "batting",
        "pitching",
        "fielding"
    )
    private val statsTypeList2 = listOf(
        "打撃成績",
        "投手成績",
        "守備成績"
    )

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // チーム名をセット
        var teamIndex = teamList.indexOf(args.teamId)
        val textView: TextView = view.findViewById(R.id.teamName)
        textView.setText(leagueId[teamIndex])

        // チームロゴをセット
        val imageView: ImageView = view.findViewById(R.id.teamLogo)
        imageView.setImageDrawable(getDrawable(view.context, leagueLogo[teamIndex]))

        // spinner(打・投・守の項目)
        val statsTypeSpinner: Spinner = view.findViewById(R.id.statsTypeSpinner)
        val statsTypeAdapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            statsTypeList2
        )
        statsTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statsTypeSpinner.adapter = statsTypeAdapter

        // spinner変更でfragment遷移するのでspinnerの中身をここでセット
        val statsIndex = statsTypeList.indexOf(args.statsType)
        statsTypeSpinner.setSelection(statsIndex)

        // spinnerの項目選択時呼び出し
        statsTypeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 初回起動時の動作を抑制して無限呼び出しを防ぐ
                if (statsTypeSpinner.isFocusable() == false) {
                    statsTypeSpinner.setFocusable(true)
                    return
                }

                // 選んだ位置のインデックス
                var statsTypeIndex = position

                // フラグメント遷移
                val action = TeamFragmentDirections.actionNavTeamToNavTeam(args.teamId, statsTypeList[statsTypeIndex])
                findNavController().navigate(action)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 選ばなかった時の動作
            }
        }

        // 初回起動時の動作を抑制して無限呼び出しを防ぐ
        statsTypeSpinner.setFocusable(false)

        val dataItem1: TextView = view.findViewById(R.id.dataItem1)
        val dataItem2: TextView = view.findViewById(R.id.dataItem2)
        val dataItem3: TextView = view.findViewById(R.id.dataItem3)
        val dataItem4: TextView = view.findViewById(R.id.dataItem4)
        val dataItem5: TextView = view.findViewById(R.id.dataItem5)
        val dataItem6: TextView = view.findViewById(R.id.dataItem6)

        dataItem1.setText("試\n合\n数")
        if(args.statsType == "batting"){
            dataItem2.setText("左\n右")
            dataItem3.setText("打\n率")
            dataItem4.setText("本\n塁\n打")
            dataItem5.setText("打\n点")
            dataItem6.setText("O\nP\nS")
        }
        else if(args.statsType == "pitching"){
            dataItem2.setText("左\n右")
            dataItem3.setText("防\n御\n率")
            dataItem4.setText("勝\n利")
            dataItem5.setText("敗\n戦")
            dataItem6.setText("イ\nニ\nン\nグ")
        }
        else if(args.statsType == "fielding"){
            dataItem2.setText("守\n備\n位\n置")
            dataItem3.setText("失\n策")
            dataItem4.setText("守\n備\n率")
            dataItem5.setText("")
            dataItem6.setText("")
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        onParallelGetInfo(args.teamId, args.statsType, recyclerView)
    }

    fun onClickData(tappedView: View, rowData: RowData) {
        //Snackbar.make(tappedView, "${rowData.name}の個人ページに飛ぶ", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        // 選手一覧から選手詳細ページへ
        val action = TeamFragmentDirections.actionNavTeamToNavIndividual(args.teamId, args.statsType, rowData.name)
        findNavController().navigate(action)
    }

    //非同期処理でHTTP GETを実行
    fun onParallelGetInfo(teamId: String?, statsType: String?, recyclerView: RecyclerView) = GlobalScope.launch(Dispatchers.Main) {
        val http = HttpUtil()
        var URL = "http://10.0.2.2:8000/api/$statsType/?team=$teamId" //ローカルホスト
        val dataList = mutableListOf<RowData>()
        do {
            async(Dispatchers.Default) {
                var body = http.httpGET1(URL)
                val result = Json.parse(body).asObject()
                val result2 = result.get("results").asArray()

                if (statsType == "batting"){ // 打撃成績取得
                    dataList.addAll(battingDataProcess(result2))
                }
                else if(statsType == "pitching"){ // 投球成績取得
                    dataList.addAll(pitchingDataProcess(result2))
                }
                else if(statsType == "fielding"){ // 守備成績取得
                    dataList.addAll(fieldingDataProcess(result2))
                }
                URL = result.get("next").toString().replace("\"", "")
            }.await()
        }while(URL != "null")


        viewAdapter = RecyclerAdapter(dataList, object : RecyclerAdapter.ListListener{
            override fun onClickData(tappedView: View, rowData: RowData) {
                this@TeamFragment.onClickData(tappedView, rowData)
            }
        })
        viewManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter

    }

    fun battingDataProcess(result:JsonArray): MutableList<RowData>{
        val dataList = mutableListOf<RowData>()
        for (res in result) {
            val data: RowData = RowData().also {stats ->
                stats.name = res.asObject().get("name").asString()
                stats.data1 = res.asObject().get("games").toString() // 試合数
                stats.data2 = res.asObject().get("handed").toString().replace("\"", "") // 左右
                stats.data3 = res.asObject().get("batting_average").toString().replace("\"", "") // 打率
                stats.data4 = res.asObject().get("homeruns").toString() // 本塁打
                stats.data5 = res.asObject().get("runs_batted_in").toString() // 打点
                stats.data6 = res.asObject().get("ops").toString().replace("\"", "") // OPS
            }
            dataList.add(data)
        }
        return dataList
    }

    fun pitchingDataProcess(result:JsonArray): MutableList<RowData>{
        val dataList = mutableListOf<RowData>()
        for (res in result) {
            val data: RowData = RowData().also {stats ->
                stats.name = res.asObject().get("name").asString()
                stats.data1 = res.asObject().get("games").toString() // 試合数
                stats.data2 = res.asObject().get("handed").toString().replace("\"", "") // 左右
                stats.data3 = res.asObject().get("earned_run_average").toString().replace("\"", "") // 防御率
                stats.data4 = res.asObject().get("wins").toString() // 勝利
                stats.data5 = res.asObject().get("loses").toString() // 敗戦
                stats.data6 = res.asObject().get("innings").toString() // イニング
            }
            dataList.add(data)
        }
        return dataList
    }

    fun fieldingDataProcess(result:JsonArray): MutableList<RowData>{
        val dataList = mutableListOf<RowData>()
        for (res in result) {
            val data: RowData = RowData().also {stats ->
                stats.name = res.asObject().get("name").asString()
                stats.data1 = res.asObject().get("games").toString() // 試合数
                stats.data2 = res.asObject().get("position").toString() // .replace("\"", "") // 守備位置
                stats.data3 = res.asObject().get("errors").toString() // 失策
                stats.data4 = res.asObject().get("fielding_average").toString().replace("\"", "") // 守備率
            }
            dataList.add(data)
        }
        return dataList
    }
}
