package io.github.m_mememe.npb_stats_ref


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eclipsesource.json.Json
import com.eclipsesource.json.JsonArray
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TeamFragment : Fragment() {
    //private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var param1: String? = null
    private var param2: String? = null
    private var rootview: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataItem1: TextView = view.findViewById(R.id.dataItem1)
        val dataItem2: TextView = view.findViewById(R.id.dataItem2)
        val dataItem3: TextView = view.findViewById(R.id.dataItem3)
        val dataItem4: TextView = view.findViewById(R.id.dataItem4)
        val dataItem5: TextView = view.findViewById(R.id.dataItem5)
        val dataItem6: TextView = view.findViewById(R.id.dataItem6)

        dataItem1.setText("試\n合\n数")
        if(param2 == "batting"){
            dataItem2.setText("左\n右")
            dataItem3.setText("打\n率")
            dataItem4.setText("本\n塁\n打")
            dataItem5.setText("打\n点")
            dataItem6.setText("O\nP\nS")
        }
        else if(param2 == "pitching"){
            dataItem2.setText("左\n右")
            dataItem3.setText("防\n御\n率")
            dataItem4.setText("勝\n利")
            dataItem5.setText("敗\n北")
            dataItem6.setText("投\n球\n回")
        }
        else if(param2 == "fielding"){
            dataItem2.setText("守\n備\n位\n置")
            dataItem3.setText("失\n策")
            dataItem4.setText("守\n備\n率")
            dataItem5.setText("")
            dataItem6.setText("")
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        onParallelGetInfo(param1, param2, recyclerView)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String): TeamFragment {
            val teamFlagment = TeamFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            teamFlagment.arguments = args
            return teamFlagment
        }
    }

    fun onClickData(tappedView: View, rowData: RowData) {
        Snackbar.make(tappedView, "${rowData.name}の個人ページに飛ぶ", Snackbar.LENGTH_LONG).setAction("Action", null).show()
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
                stats.data4 = res.asObject().get("homeruns").toString() // 本塁打
                stats.data5 = res.asObject().get("runs_batted_in").toString() // 打点
                var hits = res.asObject().get("hits").toString().toInt() // 安打
                if (hits == 0) {
                    stats.data3 = ".000"
                    stats.data6 = ".000"
                }
                else{
                    var plate_appearances = res.asObject().get("plate_appearances").toString().toInt() // 打席数
                    var bases_on_balls = res.asObject().get("bases_on_balls").toString().toInt() // 四球
                    var hits_by_pitch = res.asObject().get("hits_by_pitch").toString().toInt() // 死球
                    var sacrifice_hits = res.asObject().get("sacrifice_hits").toString().toInt() // 犠打
                    var sacrifice_flies = res.asObject().get("sacrifice_flies").toString().toInt() // 犠飛
                    var at_bat = plate_appearances.toFloat() - (bases_on_balls + hits_by_pitch + sacrifice_hits + sacrifice_flies).toFloat() // 打数

                    var on_base_percentage = (hits + bases_on_balls + hits_by_pitch).toFloat() / (plate_appearances - sacrifice_hits).toFloat() // 出塁率
                    var hits2 = res.asObject().get("hits2").toString().toInt() // 二塁打
                    var hits3 = res.asObject().get("hits3").toString().toInt() // 三塁打
                    var homeruns = res.asObject().get("homeruns").toString().toInt() // 本塁打
                    var total_bases = hits + hits2 + hits3 + homeruns // 塁打
                    var slugging_percentage = total_bases.toFloat() / at_bat // 長打率
                    var battingAverage = hits.toFloat() / at_bat // 打率
                    var OPS = on_base_percentage + slugging_percentage // OPS
                    if (battingAverage < 1.0f)
                        stats.data3 = "." + (kotlin.math.round(battingAverage * 1000.0f)).toInt().toString() // 打率
                    else
                        stats.data3 = "1.000" // 打率
                    if (OPS < 1.0f)
                        stats.data6 = "." + (kotlin.math.round(OPS * 1000.0f)).toInt().toString() // OPS
                    else
                        stats.data6 = String.format("%.3f", OPS) // OPS
                }
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
                stats.data4 = res.asObject().get("wins").toString() // 勝利
                stats.data5 = res.asObject().get("loses").toString() // 敗北
                var outs = res.asObject().get("outs").toString().toInt() // アウト
                var inning = (outs / 3).toFloat() + (outs % 3).toFloat() * 0.1 // 投球回
                stats.data6 = String.format("%.1f", inning) // 投球回
                if (outs == 0) {
                    stats.data3 = "-" // 防御率
                }
                else{
                    var earned_runs = res.asObject().get("earned_runs").toString().toInt() // 自責点
                    var earned_run_average = (earned_runs * 9 * 3).toFloat() / outs.toFloat() // 防御率
                    stats.data3 = String.format("%.2f", earned_run_average) // 防御率
                }
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
                var put_outs = res.asObject().get("put_outs").toString().toInt() // 刺殺
                var assists = res.asObject().get("assists").toString().toInt() // 補殺
                var errors = res.asObject().get("errors").toString().toInt() // 失策
                var defense_ratio = (put_outs + assists).toFloat() / (put_outs + assists + errors) // 守備率
                if (defense_ratio < 1.0f)
                    stats.data4 = "." + (kotlin.math.round(defense_ratio * 1000.0f)).toInt().toString() // 守備率
                else
                    stats.data4 = "1.000" // 守備率

            }
            dataList.add(data)
        }
        return dataList
    }
}
