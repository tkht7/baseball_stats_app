package io.github.m_mememe.npb_stats_ref

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.eclipsesource.json.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader

class TeamActivity : AppCompatActivity() {
//    val URL = "http://weather.livedoor.com/forecast/webservice/json/v1?city=400040" //サンプル:ライブドアのお天気Webサービス
    val URL = "http://10.0.2.2:8000/api/batting/" //ローカルホストを叩いてみる

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)

//        val intent: Intent = getIntent()
//        val teamName: String = intent.getStringExtra("buttonText")
//        val textView: TextView = findViewById(R.id.textView)
//        textView.setText(teamName + "の選手一覧表示")

        // Activity起動時に実行
        onParallelGetInfo()
    }

    //非同期処理でHTTP GETを実行
    fun onParallelGetInfo() = GlobalScope.launch(Dispatchers.Main) {
        val http = HttpUtil()
        //Mainスレッドでネットワーク関連処理を実行するとエラーになるためBackgroundで実行
        async(Dispatchers.Default) { http.httpGET1(URL) }.await().let {
            //minimal-jsonを使って　jsonをパース
            val result = Json.parse(it).asObject()
            val textView = findViewById(R.id.textView) as TextView

            val result2 = result.get("results").asArray()
            val result3 = result2[0].asObject()
            val result4 = result3.get("name").asString()
            textView.setText(result4)

        }
    }


}
