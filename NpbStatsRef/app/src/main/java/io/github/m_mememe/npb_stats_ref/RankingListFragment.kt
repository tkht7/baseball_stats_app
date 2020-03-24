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



class RankingListFragment : Fragment() {
    // 画面遷移時の引数受け取り
    private val args: RankingListFragmentArgs by navArgs()

    private val leagueId = ItemManagement().leagueId
    private val leagueLogo = ItemManagement().leagueLogo
    private val teamList = ItemManagement().teamList
    private val teamList2 = ItemManagement().teamList2
    private val statsTypeList3 = ItemManagement().statsTypeList3
    private val statsTypeList4 = ItemManagement().statsTypeList4
    private val rankingTeamList = ItemManagement().rankingTeamList
    private val rankingTeamList2 = ItemManagement().rankingTeamList2
    private val battingRankingList = ItemManagement().battingRankingList
    private val pitchingRankingList = ItemManagement().pitchingRankingList
    private val battingRankingList2 = ItemManagement().battingRankingList2
    private val pitchingRankingList2 = ItemManagement().pitchingRankingList2
    private val battingRankingListURL = ItemManagement().battingRankingListURL
    private val pitchingRankingListURL = ItemManagement().pitchingRankingListURL

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    val regularBatNum = 443

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ranking_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var teamIndex = rankingTeamList.indexOf(args.teamId)
        val teamName: TextView = view.findViewById(R.id.teamName)
        val imageView: ImageView = view.findViewById(R.id.teamLogo)
        var regularBat = args.regularBat
        var order = args.order
        var rankingList = listOf<String>()
        var rankingList2 = listOf<String>()
        if (args.statsType == "batting"){
            rankingList = battingRankingList
            rankingList2 = battingRankingList2
        }
        else {
            rankingList = pitchingRankingList
            rankingList2 = pitchingRankingList2
        }

        if(args.teamId == "all"){
            teamName.setText("全体ランキング")
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
        // spinnerの中身をセット
        teamSpinner.setSelection(teamIndex)

        // spinner(打・投の項目)
        val statsTypeSpinner: Spinner = view.findViewById(R.id.statsTypeSpinner)
        val statsTypeAdapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            statsTypeList4
        )
        statsTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statsTypeSpinner.adapter = statsTypeAdapter
        // spinnerの中身をセット
        var statsTypeIndex = statsTypeList3.indexOf(args.statsType)
        statsTypeSpinner.setSelection(statsTypeIndex)



        // spinner(打・投各成績の項目)
        lateinit var statsAdapter: Adapter
        var statsIndex = 0

        val statsSpinner: Spinner = view.findViewById(R.id.statsSpinner)
        statsAdapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            rankingList2
        )
        statsIndex = rankingList.indexOf(args.stats)

        statsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statsSpinner.adapter = statsAdapter
        // spinnerの中身をセット
        statsSpinner.setSelection(statsIndex)


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

                // フラグメント遷移
                val action = RankingListFragmentDirections.actionNavRankingListSelf(rankingTeamList[teamIndex], statsTypeList3[statsTypeIndex], rankingList[statsIndex], regularBat, order)
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
                if (statsTypeIndex == 0)
                    rankingList = battingRankingList
                else
                    rankingList = pitchingRankingList
                if (statsTypeList3[statsTypeIndex] != args.statsType)
                    statsIndex = 0
                if (statsTypeIndex == 1){
                    regularBat = false
                }

                // フラグメント遷移
                val action = RankingListFragmentDirections.actionNavRankingListSelf(rankingTeamList[teamIndex], statsTypeList3[statsTypeIndex], rankingList[statsIndex], regularBat, order)
                findNavController().navigate(action)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 選ばなかった時の動作
            }
        }

        // 初回起動時の動作を抑制して無限呼び出しを防ぐ
        statsTypeSpinner.setFocusable(false)

        // statsSpinnerの項目選択時呼び出し
        statsSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 初回起動時の動作を抑制して無限呼び出しを防ぐ
                if (statsSpinner.isFocusable() == false) {
                    statsSpinner.setFocusable(true)
                    return
                }

                // positionは選んだ位置のインデックス
                statsIndex = position

                // フラグメント遷移
                val action = RankingListFragmentDirections.actionNavRankingListSelf(rankingTeamList[teamIndex], statsTypeList3[statsTypeIndex], rankingList[statsIndex], regularBat, order)
                findNavController().navigate(action)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 選ばなかった時の動作
            }
        }

        // 初回起動時の動作を抑制して無限呼び出しを防ぐ
        statsSpinner.setFocusable(false)

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
            val action = RankingListFragmentDirections.actionNavRankingListSelf(rankingTeamList[teamIndex], statsTypeList3[statsTypeIndex], rankingList[statsIndex], regularBat, order)
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
                val action = RankingListFragmentDirections.actionNavRankingListSelf(rankingTeamList[teamIndex], statsTypeList3[statsTypeIndex], rankingList[statsIndex], regularBat, order)
                findNavController().navigate(action)
            }
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        onParallelGetInfo(args.teamId, args.statsType, args.stats, args.regularBat, args.order, recyclerView)
    }

    fun onClickData(tappedView: View, data: RankingListData) {
        // 個人ページへ
        var team = rankingTeamList[rankingTeamList2.indexOf(data.team)]
        val action = RankingListFragmentDirections.actionNavRankingListToNavIndividual(team, args.statsType, data.name)
        findNavController().navigate(action)
    }

    //非同期処理でHTTP GETを実行
    fun onParallelGetInfo(teamId: String?, statsType: String?, stats: String?, regularBat: Boolean, order: Boolean,recyclerView: RecyclerView) = GlobalScope.launch(Dispatchers.Main) {
        val http = HttpUtil()
        var URL = "http://10.0.2.2:8000/api/$statsType/?team=$teamId" //ローカルホスト
        if (teamId == "all")
            URL = "http://10.0.2.2:8000/api/$statsType/"
        if (regularBat)
            URL += if (URL.substring(URL.length-1) == "/") "?pa_r_min=$regularBatNum" else "&pa_r_min=$regularBatNum"
        var rankingList = listOf<String>()
        var rankingListURL = listOf<String>()
        if (args.statsType == "batting"){
            rankingList = battingRankingList
            rankingListURL = battingRankingListURL
        }
        else {
            rankingList = pitchingRankingList
            rankingListURL = pitchingRankingListURL
        }
        val statsURL = rankingListURL[rankingList.indexOf(stats)]
        if (order)
            URL += if (URL.substring(URL.length-1) == "/") "?order_by=$statsURL" else "&order_by=$statsURL"
        else
            URL += if (URL.substring(URL.length-1) == "/") "?order_by=-$statsURL" else "&order_by=-$statsURL"
        val dataList = mutableListOf<RankingListData>()

        async(Dispatchers.Default) {
            var body = http.httpGET1(URL)
            val result = Json.parse(body).asObject()
            val result2 = result.get("results").asArray()
            // ランキングデータを取得
            dataList.addAll(DataProcess(result2, statsType, stats))
        }.await()

        viewAdapter = RankingListRecyclerAdapter(dataList, object : RankingListRecyclerAdapter.ListListener{
            override fun onClickData(tappedView: View, data: RankingListData) {
                this@RankingListFragment.onClickData(tappedView, data)
            }
        })
        viewManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter

    }

    fun DataProcess(result:JsonArray, statsType: String?, stats: String?): MutableList<RankingListData>{
        val dataList = mutableListOf<RankingListData>()
        var rank = 0
        for (res in result) {
            rank += 1
            val data: RankingListData = RankingListData().also { st ->
                st.order = rank.toString()+"位" //
                st.team = rankingTeamList2[rankingTeamList.indexOf(res.asObject().get("team").asString())]
                st.name = res.asObject().get("name").asString()
                st.stats = res.asObject().get("$stats").toString().replace("\"", "") //
            }
            dataList.add(data)
        }
        return dataList
    }
}
