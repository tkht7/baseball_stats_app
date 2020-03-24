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
import com.eclipsesource.json.JsonValue
import kotlinx.android.synthetic.main.fragment_ranking_top.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch



class RankingTopFragment : Fragment() {
    // 画面遷移時の引数受け取り
    private val args: RankingTopFragmentArgs by navArgs()

    private val leagueId = ItemManagement().leagueId
    private val leagueLogo = ItemManagement().leagueLogo
    private val teamList = ItemManagement().teamList
    private val teamList2 = ItemManagement().teamList2
    private val rankingTeamList = ItemManagement().rankingTeamList
    private val rankingTeamList2 = ItemManagement().rankingTeamList2
    private val statsTypeList3 = ItemManagement().statsTypeList3
    private val statsTypeList4 = ItemManagement().statsTypeList4
    private val battingRankingList = ItemManagement().battingRankingList
    private val pitchingRankingList = ItemManagement().pitchingRankingList
    private val battingRankingList2 = ItemManagement().battingRankingList2
    private val pitchingRankingList2 = ItemManagement().pitchingRankingList2

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    val regularBatNum = 443

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ranking_top, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var teamIndex = rankingTeamList.indexOf(args.teamId)
        val teamName: TextView = view.findViewById(R.id.teamName)
        val imageView: ImageView = view.findViewById(R.id.teamLogo)
        var regularBat = args.regularBat
        var order = args.order

        if(args.teamId == "all"){
            teamName.setText("全体ランキング") // 全体
        }
        else{

            teamName.setText(leagueId[teamIndex]) // チーム名をセット
            imageView.setImageDrawable(getDrawable(view.context, leagueLogo[teamIndex])) // チームロゴをセット
        }

        // spinner(チームの項目)
        val teamSpinner: Spinner = view.findViewById(R.id.teamSpinner)
        val teamAdapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            rankingTeamList2
        )
        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        teamSpinner.adapter = teamAdapter
        // spinner変更でfragment遷移するのでspinnerの中身をここでセット
        teamSpinner.setSelection(teamIndex)

        // spinner(打・投・守の項目)
        val statsTypeSpinner: Spinner = view.findViewById(R.id.statsTypeSpinner)
        val statsTypeAdapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            statsTypeList4
        )
        statsTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statsTypeSpinner.adapter = statsTypeAdapter
        // spinner変更でfragment遷移するのでspinnerの中身をここでセット
        var statsTypeIndex = statsTypeList3.indexOf(args.statsType)
        statsTypeSpinner.setSelection(statsTypeIndex)

        // teamSpinnerの項目選択時呼び出し
        teamSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 初回起動時の動作を抑制して無限呼び出しを防ぐ
                if (teamSpinner.isFocusable() == false) {
                    teamSpinner.setFocusable(true)
                    return
                }

                // positionは選んだ位置のインデックス
                teamIndex = position

                // フラグメント遷移 positionは選んだ位置のインデックス
                val action = RankingTopFragmentDirections.actionNavRankingTopSelf(rankingTeamList[teamIndex], statsTypeList3[statsTypeIndex], regularBat, order)
                findNavController().navigate(action)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 選ばなかった時の動作
            }
        }
        // 初回起動時の動作を抑制して無限呼び出しを防ぐ
        teamSpinner.setFocusable(false)

        // statsTypeSpinnerの項目選択時呼び出し
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

                // positionは選んだ位置のインデックス
                statsTypeIndex = position
                if (statsTypeIndex == 1){
                    regularBat = false
                }

                // フラグメント遷移
                val action = RankingTopFragmentDirections.actionNavRankingTopSelf(rankingTeamList[teamIndex], statsTypeList3[statsTypeIndex], regularBat, order)
                findNavController().navigate(action)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 選ばなかった時の動作
            }
        }

        // 初回起動時の動作を抑制して無限呼び出しを防ぐ
        statsTypeSpinner.setFocusable(false)

        // 昇順/降順切替スイッチ
        val orderSwitch = view.findViewById<Switch>(R.id.orderSwitch)
        orderSwitch.isChecked = order
        orderSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                order = true
            } else {
                order = false
            }
            // フラグメント遷移
            val action = RankingTopFragmentDirections.actionNavRankingTopSelf(rankingTeamList[teamIndex], statsTypeList3[statsTypeIndex], regularBat, order)
            findNavController().navigate(action)
        }

        // 規定打席到達フィルタ
        val regularBatSwitch = view.findViewById<Switch>(R.id.regularBatFilter)
        regularBatSwitch.isChecked = regularBat
        regularBatSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                regularBat = true
            } else {
                regularBat = false
            }
            if (statsTypeIndex == 1){
                regularBat = false
                regularBatSwitch.isChecked = regularBat
            }
            else {
                // フラグメント遷移
                val action = RankingTopFragmentDirections.actionNavRankingTopSelf(rankingTeamList[teamIndex], statsTypeList3[statsTypeIndex], regularBat, order)
                findNavController().navigate(action)
            }
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        onParallelGetInfo(args.teamId, args.statsType, args.regularBat, args.order, recyclerView)
    }

    fun onClickData(tappedView: View, data: RankingTopData, position: Int) {
        var rankingList = listOf<String>()
        if (args.statsType == "batting")
            rankingList = battingRankingList
        else
            rankingList = pitchingRankingList
        // ランキング一覧ページへ
        val action = RankingTopFragmentDirections.actionNavRankingTopToNavRankingList(args.teamId, args.statsType, rankingList[position], args.regularBat, args.order)
        findNavController().navigate(action)
    }

    //非同期処理でHTTP GETを実行
    fun onParallelGetInfo(teamId: String?, statsType: String?, regularBat: Boolean, order: Boolean, recyclerView: RecyclerView) = GlobalScope.launch(Dispatchers.Main) {
        val http = HttpUtil()
        var URL = "http://10.0.2.2:8000/api/$statsType/?team=$teamId" //ローカルホスト
        if (teamId == "all")
            URL = "http://10.0.2.2:8000/api/$statsType"
        if (regularBat) //
            URL += if(teamId=="all") "/?pa_r_min=$regularBatNum" else "&pa_r_min=$regularBatNum"
        val dataList = mutableListOf<RankingTopData>()
        var result = mutableListOf<JsonValue>()
        async(Dispatchers.Default) {
            do {
                var body = http.httpGET1(URL)
                val result1 = Json.parse(body).asObject()
                val result2 = result1.get("results").asArray()
                for(res in result2){
                    result.add(res)
                }
                URL = result1.get("next").toString().replace("\"", "")
            }while(URL != "null")
        }.await()
        // ランキングデータを取得
        dataList.addAll(DataProcess(result, statsType, order))

        viewAdapter = RankingTopRecyclerAdapter(dataList, object : RankingTopRecyclerAdapter.ListListener{
            override fun onClickData(tappedView: View, data: RankingTopData, position: Int) {
                this@RankingTopFragment.onClickData(tappedView, data, position)
            }
        })
        viewManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter

    }

    fun DataProcess(result:MutableList<JsonValue>, statsType: String?, order: Boolean): MutableList<RankingTopData>{
        val dataList = mutableListOf<RankingTopData>()
        var rankingList: List<String>
        var rankingList2: List<String>
        if (statsType == "batting"){
            rankingList = battingRankingList
            rankingList2 = battingRankingList2
        }
        else {
            rankingList = pitchingRankingList
            rankingList2 = pitchingRankingList2
        }
        // 初期化
        for (i in 0..rankingList.size-1) {
            val data: RankingTopData = RankingTopData().also {stats ->
                stats.item = rankingList2[i]
                stats.name = ""
                stats.stats = ""
            }
            dataList.add(data)
        }
        // ランキングトップを探す
        for (res in result) {
            for (i in 0..rankingList.size-1) {
                val data: RankingTopData = RankingTopData().also { stats ->
                    //stats.item = rankingList2[i] //
                    stats.name = res.asObject().get("name").asString()
                    stats.stats = res.asObject().get(rankingList[i]).toString().replace("\"", "") //
                }
                if(data.stats == "-") continue
                if (dataList[i].stats == ""){
                    dataList[i].name = data.name
                    dataList[i].stats = data.stats
                }
                else if(order) {
                    if (data.stats.replace(".", "").toInt() < dataList[i].stats.replace(".","").toInt()) {
                        dataList[i].name = data.name
                        dataList[i].stats = data.stats
                    }
                }
                else {
                    if (data.stats.replace(".", "").toInt() > dataList[i].stats.replace(".","").toInt()) {
                        dataList[i].name = data.name
                        dataList[i].stats = data.stats
                    }
                }
            }
        }
        return dataList
    }
}
