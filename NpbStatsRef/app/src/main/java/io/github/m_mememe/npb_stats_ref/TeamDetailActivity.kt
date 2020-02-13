package io.github.m_mememe.npb_stats_ref

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class TeamDetailActivity : AppCompatActivity() {
    //teamNameに応じたqueryListを作成してWebAPIを叩く
    private val leagueId = MainActivity().leagueId
    private val queryList = listOf<String>(
        "lions",        //西武ライオンズ
        "hawks",        //ソフトバンク
        "eagles",       //楽天
        "marines",      //千葉ロッテ
        "fighters",     //日本ハム
        "buffaloes",    //オリックス
        "giants",       //ジャイアンツ
        "baystars",     //DeNA
        "tigers",       //阪神
        "carp",         //カープ
        "dragons",      //中日
        "swallows"      //ヤクルト
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)
        val teamName = intent.getIntExtra("teamName", 0)
        val name = findViewById<TextView>(R.id.nnn)
        name.text = getString(teamName)
        //leagueIdからteamNameを検索してそのインデックスを返したい、BinarySearchはソートした配列にしか使えないのでダメ
        var teamIndex = 0
        for (_teamName in leagueId){
            if (teamName == _teamName) break
            teamIndex += 1
        }
        Toast.makeText(this, queryList[teamIndex].toString(), Toast.LENGTH_SHORT).show()

        val receiver = TeamDetailReceiver()
        receiver.execute("batting")
    }

    private inner class TeamDetailReceiver : AsyncTask<String, String, String>(){
        override fun doInBackground(vararg params: String?): String {
            val id = params[0]
            val urlStr = "http://10.0.2.2:80/api/$id"
            val url = URL(urlStr)
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.connect()
            val stream = con.inputStream
            val result = is2String(stream)
            con.disconnect()
            stream.close()

            return result
        }

        override fun onPostExecute(result: String?) {
            result?.let {
                val rootJSON = JSONObject(it)
                val resultString = rootJSON.getJSONArray("results")[0].toString()
                val resultJSON = JSONObject(resultString)
                val playerName = resultJSON.getString("name")
                val tvNnn = findViewById<TextView>(R.id.nnn)
                tvNnn.text = playerName.toString()
            }
        }

        private fun is2String(stream: InputStream): String{
            val sb = StringBuilder()
            val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
            var line = reader.readLine()
            while(line != null){
                sb.append(line)
                line = reader.readLine()
            }
            reader.close()
            return sb.toString()
        }
    }
}
