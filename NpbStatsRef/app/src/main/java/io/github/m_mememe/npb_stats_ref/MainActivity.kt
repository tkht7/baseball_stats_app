package io.github.m_mememe.npb_stats_ref

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    val paNum = 6
    val seNum = 6

    val leagueLogo = listOf(
        // パ・リーグ
        R.drawable.lions,
        R.drawable.hawks,
        R.drawable.eagles,
        R.drawable.marines,
        R.drawable.fighters,
        R.drawable.buffaloes,
        // セ・リーグ
        R.drawable.giants,
        R.drawable.baystars,
        R.drawable.tigers,
        R.drawable.carp,
        R.drawable.dragons,
        R.drawable.swallows
    )

    val leagueId = listOf(
        // パ・リーグ
        R.string.lions,
        R.string.hawks,
        R.string.eagles,
        R.string.marines,
        R.string.fighters,
        R.string.buffaloes,
        // セ・リーグ
        R.string.giants,
        R.string.baystars,
        R.string.tigers,
        R.string.carp,
        R.string.dragons,
        R.string.swallows
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //セとパそれぞれでListViewを作成する
        val lvPaLeague = findViewById<ListView>(R.id.lvPaLeague)
        val adapterPa = LeagueAdapter(this, R.layout.list_league, leagueLogo.subList(0, paNum), leagueId.subList(0, paNum))
        val headerPa = layoutInflater.inflate(R.layout.header_league, null)
        val headerPaText = headerPa.findViewById<TextView>(R.id.tvLeague)
        headerPa.setBackgroundColor(Color.RED)
        headerPaText.text = getString(R.string.pa_league)
        lvPaLeague.addHeaderView(headerPa, null, false)
        lvPaLeague.adapter = adapterPa
        lvPaLeague.onItemClickListener = ListItemClickListener()

        val lvSeLeague = findViewById<ListView>(R.id.lvSeLeague)
        val adapterSe = LeagueAdapter(this, R.layout.list_league, leagueLogo.subList(paNum, paNum+seNum), leagueId.subList(paNum, paNum+seNum))
        val headerSe = layoutInflater.inflate(R.layout.header_league, null)
        val headerSeText = headerSe.findViewById<TextView>(R.id.tvLeague)
        headerSe.setBackgroundColor(Color.BLUE)
        headerSeText.text = getString(R.string.se_league)
        lvSeLeague.addHeaderView(headerSe, null, false)
        lvSeLeague.adapter = adapterSe
        lvSeLeague.onItemClickListener = ListItemClickListener(paNum)
    }

    private inner class ListItemClickListener(bias: Int = 0): AdapterView.OnItemClickListener{
        var cBias = bias
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val intent = Intent(this@MainActivity, TeamActivity::class.java)
            //positionは1から帰ってくるので"-1"する
            val selectedText = leagueId[position + cBias - 1]
            intent.putExtra("teamName", selectedText)
            startActivity(intent)
        }
    }
}
