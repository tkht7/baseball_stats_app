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



class IndividualFragment : Fragment() {
    // 画面遷移時の引数受け取り
    private val args: IndividualFragmentArgs by navArgs()

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
    private val battingItemList = listOf(
        "games",
        "handed",
        "batting_average",
        "plate_appearances",
        "at_bats",
        "runs",
        "hits",
        "hits2",
        "hits3",
        "homeruns",
        "runs_batted_in",
        "strike_outs",
        "bases_on_balls",
        "intentional_walks",
        "sacrifice_hits",
        "sacrifice_flies",
        "steals",
        "caught_steals",
        "stolen_bases_percentage",
        "double_plays",
        "on_base_percentage",
        "slugging_percentage",
        "ops"
    )
    private val pitchingItemList = listOf(
        "games",
        "handed",
        "earned_run_average",
        "wins",
        "loses",
        "holds",
        "saves",
        "completes",
        "shut_out_wins",
        "winning_percentage",
        "innings",
        "batters_faced",
        "hits",
        "homeruns",
        "strike_outs",
        "bases_on_balls",
        "intentional_walks",
        "hits_by_pitch",
        "wild_pitches",
        "balks",
        "runs",
        "earned_runs"
    )
    private val fieldingItemList = listOf(
        "games",
        "put_outs",
        "assists",
        "errors",
        "double_plays",
        "passed_balls",
        "fielding_average"
    )
    private val battingItemList2 = listOf(
        "試合数",
        "左右",
        "打率",
        "打席",
        "打数",
        "得点",
        "安打",
        "二塁打",
        "三塁打",
        "本塁打",
        "打点",
        "三振",
        "四球",
        "敬遠",
        "犠打",
        "犠飛",
        "盗塁",
        "盗塁死",
        "盗塁\n成功率",
        "併殺打",
        "出塁率",
        "長打率",
        "OPS"
    )
    private val pitchingItemList2 = listOf(
        "試合数",
        "左右",
        "防御率",
        "勝利",
        "敗戦",
        "ホールド",
        "セーブ",
        "完投",
        "完封",
        "勝率",
        "イニング",
        "対戦\n打者数",
        "被安打",
        "被本塁打",
        "奪三振",
        "与四球",
        "故意四球",
        "与死球",
        "暴投",
        "ボーク",
        "失点",
        "自責点"
    )
    private val fieldingItemList2 = listOf(
        "試合数",
        "刺殺",
        "補殺",
        "失策",
        "併殺",
        "捕逸",
        "守備率"
    )

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_individual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // チーム名をセット
        var teamIndex = teamList.indexOf(args.teamId)
        val teamName: TextView = view.findViewById(R.id.teamName)
        teamName.setText(leagueId[teamIndex])

        // チームロゴをセット
        val imageView: ImageView = view.findViewById(R.id.teamLogo)
        imageView.setImageDrawable(getDrawable(view.context, leagueLogo[teamIndex]))

        // 写真をセット(可能なら)

        // 名前をセット
        val individualName: TextView = view.findViewById(R.id.individualName)
        individualName.setText(args.name)

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
                val action = IndividualFragmentDirections.actionNavIndividualToNavIndividual(args.teamId, statsTypeList[statsTypeIndex], args.name)
                findNavController().navigate(action)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 選ばなかった時の動作
            }
        }

        // 初回起動時の動作を抑制して無限呼び出しを防ぐ
        statsTypeSpinner.setFocusable(false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.individualRecyclerView)
        onParallelGetInfo(args.name, args.statsType, recyclerView)
    }

    fun onClickData(tappedView: View, dataForm: IndividualDataForm) {
        //Snackbar.make(tappedView, "${rowData.name}の個人ページに飛ぶ", Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

    //非同期処理でHTTP GETを実行
    fun onParallelGetInfo(name: String?, statsType: String?, recyclerView: RecyclerView) = GlobalScope.launch(Dispatchers.Main) {
        val http = HttpUtil()
        var URL = "http://10.0.2.2:8000/api/$statsType/?name=$name" //ローカルホスト
        var URL2 = "http://10.0.2.2:8000/api/fielding/?name=$name" //守備位置取得用
        val dataList = mutableListOf<IndividualDataForm>()
        var position = ""
        async(Dispatchers.Default) {
            var body = http.httpGET1(URL)
            val result = Json.parse(body).asObject()
            val result2 = result.get("results").asArray()

            if(result.get("count").toString() == "0"){
                var nodata = mutableListOf<IndividualDataForm>()
                var nodata1 = IndividualDataForm()
                nodata1.item1 = "データ\n無し"
                nodata.add(nodata1)
                dataList.addAll(nodata)
            }
            else if (statsType == "batting"){ // 打撃成績取得
                dataList.addAll(battingDataProcess(result2))
            }
            else if(statsType == "pitching"){ // 投球成績取得
                dataList.addAll(pitchingDataProcess(result2))
            }
            else if(statsType == "fielding"){ // 守備成績取得
                dataList.addAll(fieldingDataProcess(result2))
            }
            else{

            }
            var body2 = http.httpGET1(URL2)
            position = Json.parse(body2).asObject().get("results").asArray()[0].asObject().get("position").toString()
        }.await()


        val fieldingPosition: TextView? = view?.findViewById(R.id.fieldingPosition)
        fieldingPosition?.setText("守備位置 : " + position)

        viewAdapter = IndividualRecyclerAdapter(dataList, object : IndividualRecyclerAdapter.ListListener{
            override fun onClickData(tappedView: View, dataForm: IndividualDataForm) {
                this@IndividualFragment.onClickData(tappedView, dataForm)
            }
        })
        viewManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter
    }

    fun battingDataProcess(result:JsonArray): MutableList<IndividualDataForm>{
        val dataList = mutableListOf<IndividualDataForm>()

        for (i in 0..7) {
            val data: IndividualDataForm = IndividualDataForm().also {stats ->
                if(i < battingItemList.size){
                    stats.item1 = battingItemList2[i] // 項目
                    stats.stats1 = result[0].asObject().get(battingItemList[i]).toString().replace("\"", "") // 成績
                }
                else{
                    stats.item1 = ""  // 項目
                    stats.stats1 = "" // 成績
                }
                if(i+8 < battingItemList.size){
                    stats.item2 = battingItemList2[i+8] // 項目
                    stats.stats2 = result[0].asObject().get(battingItemList[i+8]).toString().replace("\"", "") // 成績
                }
                else{
                    stats.item2 = ""  // 項目
                    stats.stats2 = "" // 成績
                }
                if(i+16 < battingItemList.size){
                    stats.item3 = battingItemList2[i+16] // 項目
                    stats.stats3 = result[0].asObject().get(battingItemList[i+16]).toString().replace("\"", "") // 成績
                }
                else{
                    stats.item3 = ""  // 項目
                    stats.stats3 = "" // 成績
                }
            }
            dataList.add(data)
        }

        return dataList
    }

    fun pitchingDataProcess(result:JsonArray): MutableList<IndividualDataForm>{
        val dataList = mutableListOf<IndividualDataForm>()

        for (i in 0..7) {
            val data: IndividualDataForm = IndividualDataForm().also {stats ->
                if(i < pitchingItemList.size){
                    stats.item1 = pitchingItemList2[i] // 項目
                    stats.stats1 = result[0].asObject().get(pitchingItemList[i]).toString().replace("\"", "") // 成績
                }
                else{
                    stats.item1 = ""  // 項目
                    stats.stats1 = "" // 成績
                }
                if(i+8 < pitchingItemList.size){
                    stats.item2 = pitchingItemList2[i+8] // 項目
                    stats.stats2 = result[0].asObject().get(pitchingItemList[i+8]).toString().replace("\"", "") // 成績
                }
                else{
                    stats.item2 = ""  // 項目
                    stats.stats2 = "" // 成績
                }
                if(i+16 < pitchingItemList.size){
                    stats.item3 = pitchingItemList2[i+16] // 項目
                    stats.stats3 = result[0].asObject().get(pitchingItemList[i+16]).toString().replace("\"", "") // 成績
                }
                else{
                    stats.item3 = ""  // 項目
                    stats.stats3 = "" // 成績
                }
            }
            dataList.add(data)
        }

        return dataList
    }

    fun fieldingDataProcess(result:JsonArray): MutableList<IndividualDataForm>{
        val dataList = mutableListOf<IndividualDataForm>()

        for (i in 0..7) {
            val data: IndividualDataForm = IndividualDataForm().also {stats ->
                if(i < fieldingItemList.size){
                    stats.item1 = fieldingItemList2[i] // 項目
                    stats.stats1 = result[0].asObject().get(fieldingItemList[i]).toString().replace("\"", "") // 成績
                }
                else{
                    stats.item1 = ""  // 項目
                    stats.stats1 = "" // 成績
                }
                if(i+8 < fieldingItemList.size){
                    stats.item2 = fieldingItemList2[i+8] // 項目
                    stats.stats2 = result[0].asObject().get(fieldingItemList[i+8]).toString().replace("\"", "") // 成績
                }
                else{
                    stats.item2 = ""  // 項目
                    stats.stats2 = "" // 成績
                }
                if(i+16 < fieldingItemList.size){
                    stats.item3 = fieldingItemList2[i+16] // 項目
                    stats.stats3 = result[0].asObject().get(fieldingItemList[i+16]).toString().replace("\"", "") // 成績
                }
                else{
                    stats.item3 = ""  // 項目
                    stats.stats3 = "" // 成績
                }
            }
            dataList.add(data)
        }

        return dataList
    }
}
