package io.github.m_mememe.npb_stats_ref

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
//    val URL = "http://weather.livedoor.com/forecast/webservice/json/v1?city=400040" //サンプル:ライブドアのお天気Webサービス
    val URL = "http://10.0.2.2:8000/api/batting/?offset=240" //ローカルホストを叩いてみる

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)

        val intent: Intent = getIntent()
        val teamName: String? = intent.getStringExtra("buttonText")
        val textView: TextView = findViewById(R.id.teamName)
        textView.setText(teamName)

        // Activity起動時に実行
        onParallelGetInfo()
    }

    fun onClickData(tappedView: View, rowData: RowData) {
        Snackbar.make(tappedView, "${rowData.name}の個人ページに飛ぶ", Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

    //非同期処理でHTTP GETを実行
    fun onParallelGetInfo() = GlobalScope.launch(Dispatchers.Main) {
        val http = HttpUtil()

        //Mainスレッドでネットワーク関連処理を実行するとエラーになるためBackgroundで実行
        async(Dispatchers.Default) { http.httpGET1(URL) }.await().let {
            //minimal-jsonを使って　jsonをパース
            val result = Json.parse(it).asObject()
            val result2 = result.get("results").asArray()

            fun createDataList(): List<RowData> {
                val dataList = mutableListOf<RowData>()
                for (res in result2) {
                    val data: RowData = RowData().also {stats ->
                        stats.name = res.asObject().get("name").asString()
                        stats.data1 = res.asObject().get("games").toString()
                        stats.data2 = res.asObject().get("plate_appearances").toString()
                        stats.data3 = res.asObject().get("runs").toString()
                        stats.data4 = res.asObject().get("hits").toString()
                        stats.data5 = res.asObject().get("homeruns").toString()
                    }
                    dataList.add(data)
                }
                return dataList
            }

            viewAdapter = RecyclerAdapter(createDataList(), object : RecyclerAdapter.ListListener{
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
}
