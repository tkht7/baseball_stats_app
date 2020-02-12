package io.github.m_mememe.npb_stats_ref

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class TeamDetailActivity : AppCompatActivity() {
    //nameに応じたMapを作成してWebAPIを叩く
    private val leagueId = MainActivity().leagueId
    private val queryList = listOf<String>(
        ""
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)
        val name = findViewById<TextView>(R.id.nnn)
        val teamName = intent.getIntExtra("teamName", 0)
        name.text = getString(teamName)
        var count = 0
        for (_name in leagueId){
            if (teamName == _name) break
            count += 1
        }
        Toast.makeText(this, count.toString(), Toast.LENGTH_SHORT).show()
    }
}
