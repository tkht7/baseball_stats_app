package io.github.m_mememe.npb_stats_ref

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import io.github.m_mememe.npb_stats_ref.TeamFragment.Companion.newInstance

class TeamActivity : AppCompatActivity() {
    //teamNameに応じたqueryListを作成してWebAPIを叩く
    private val leagueId = MainActivity().leagueId
    private val leagueLogo = MainActivity().leagueLogo
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)

        val teamName = intent.getIntExtra("teamName", 0)
        val textView: TextView = findViewById(R.id.teamName)
        textView.setText(getString(teamName))

        //leagueIdからteamNameを検索してそのインデックスを返したい、BinarySearchはソートした配列にしか使えないのでダメ
        var teamIndex = 0
        for (_teamName in leagueId) {
            if (teamName == _teamName) break
            teamIndex += 1
        }

        val imageView: ImageView = findViewById(R.id.teamLogo)
        imageView.setImageDrawable(getDrawable(leagueLogo[teamIndex]))

        var statsTypeIndex = 0
        val statsType: Spinner = findViewById(R.id.statsType)
        val statsTypeAdapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item,
            statsTypeList2
        )
        statsTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statsType.adapter = statsTypeAdapter
        statsType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                statsTypeIndex = position
                // フラグメントを呼ぶ
                if (savedInstanceState == null) {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.teamContainer, newInstance(teamList[teamIndex], statsTypeList[statsTypeIndex])) // チームと打・投・守をいれる
                    transaction.commit()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 選ばなかった時の動作
            }
        }
    }
}
