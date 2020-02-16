package io.github.m_mememe.npb_stats_ref

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eclipsesource.json.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import com.google.android.material.snackbar.Snackbar

class TeamActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    //teamNameに応じたqueryListを作成してWebAPIを叩く
    private val leagueId = MainActivity().leagueId
    private val leagueLogo = MainActivity().leagueLogo
    private val queryList = listOf<String>(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)

        val teamName = intent.getIntExtra("teamName", 0)
        val textView: TextView = findViewById(R.id.teamName)
        textView.setText(getString(teamName))

        //leagueIdからteamNameを検索してそのインデックスを返したい、BinarySearchはソートした配列にしか使えないのでダメ
        var teamIndex = 0
        for (_teamName in leagueId){
            if (teamName == _teamName) break
            teamIndex += 1
        }

        val imageView: ImageView = findViewById(R.id.teamLogo)
        imageView.setImageDrawable(getDrawable(leagueLogo[teamIndex]))

        // Activity起動時に実行
        onParallelGetInfo(queryList[teamIndex])
    }

    fun onClickData(tappedView: View, rowData: RowData) {
        Snackbar.make(tappedView, "${rowData.name}の個人ページに飛ぶ", Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

    //非同期処理でHTTP GETを実行
    fun onParallelGetInfo(teamId: String) = GlobalScope.launch(Dispatchers.Main) {
        val http = HttpUtil()
        var URL = "http://10.0.2.2:8000/api/batting/?team=$teamId" //ローカルホスト
        val dataList = mutableListOf<RowData>()

        //Mainスレッドでネットワーク関連処理を実行するとエラーになるためBackgroundで実行
        do {
            async(Dispatchers.Default) {
                var body = http.httpGET1(URL)
                val result = Json.parse(body).asObject()
                val result2 = result.get("results").asArray()

                for (res in result2) {
                    val data: RowData = RowData().also {stats ->
                        stats.name = res.asObject().get("name").asString()
                        stats.handed = res.asObject().get("handed").toString() // 左右
                        stats.homeruns = res.asObject().get("homeruns").toString() // 本塁打
                        stats.runsBattedIn = res.asObject().get("runs_batted_in").toString() // 打点
                        var hits = res.asObject().get("hits").toString().toInt() // 安打
                        if (hits == 0) {
                            stats.battingAverage = ".000"
                            stats.OPS = ".000"
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
                                stats.battingAverage = "." + (kotlin.math.round(battingAverage * 1000.0f)).toInt().toString() // 打率
                            else
                                stats.battingAverage = "1.000" // 打率
                            if (OPS < 1.0f)
                                stats.OPS = "." + (kotlin.math.round(OPS * 1000.0f)).toInt().toString() // OPS
                            else
                                stats.OPS = String.format("%.3f", OPS) // OPS
                        }
                    }
                    dataList.add(data)
                }
                URL = result.get("next").toString().replace("\"", "")
            }.await()
        }while(URL != "null")

        viewAdapter = RecyclerAdapter(dataList, object : RecyclerAdapter.ListListener{
            override fun onClickData(tappedView: View, rowData: RowData) {
                this@TeamActivity.onClickData(tappedView, rowData)
            }
        })
        viewManager = LinearLayoutManager(this@TeamActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}
