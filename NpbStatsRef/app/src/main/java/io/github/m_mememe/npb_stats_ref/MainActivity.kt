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
    private val seLogo = listOf(
        R.drawable.giants,
        R.drawable.baystars,
        R.drawable.tigers,
        R.drawable.carp,
        R.drawable.dragons,
        R.drawable.swallows
    )
    private val paLogo = listOf(
        R.drawable.baystars,
        R.drawable.baystars,
        R.drawable.baystars,
        R.drawable.baystars,
        R.drawable.baystars,
        R.drawable.baystars
    )
    private val seLeague = listOf(
        R.string.giants,
        R.string.baystars,
        R.string.tigers,
        R.string.carp,
        R.string.dragons,
        R.string.swallows
    )
    private val paLeague = listOf(
        R.string.lions,
        R.string.hawks,
        R.string.eagles,
        R.string.marines,
        R.string.fighters,
        R.string.buffaloes
    )
    val leagueId = paLeague.plus(seLeague)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //セとパそれぞれでListViewを作成する
        val lvPaLeague = findViewById<ListView>(R.id.lvPaLeague)
        val adapterPa = LeagueAdapter(this, R.layout.list_league, paLogo, paLeague)
        val headerPa = layoutInflater.inflate(R.layout.header_league, null)
        val headerPaText = headerPa.findViewById<TextView>(R.id.tvLeague)
        headerPa.setBackgroundColor(Color.RED)
        headerPaText.text = getString(R.string.pa_league)
        lvPaLeague.addHeaderView(headerPa, null, false)
        lvPaLeague.adapter = adapterPa
        lvPaLeague.onItemClickListener = ListItemClickListenerPa()

        val lvSeLeague = findViewById<ListView>(R.id.lvSeLeague)
        val adapterSe = LeagueAdapter(this, R.layout.list_league, seLogo, seLeague)
        val headerSe = layoutInflater.inflate(R.layout.header_league, null)
        val headerSeText = headerSe.findViewById<TextView>(R.id.tvLeague)
        headerSe.setBackgroundColor(Color.BLUE)
        headerSeText.text = getString(R.string.se_league)
        lvSeLeague.addHeaderView(headerSe, null, false)
        lvSeLeague.adapter = adapterSe
        lvSeLeague.onItemClickListener = ListItemClickListenerSe()
    }

    private inner class ListItemClickListenerPa: AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val intent = Intent(applicationContext, TeamDetailActivity::class.java)
            //positionは1から帰ってくるので"-1"する
            val selectedText = paLeague[position - 1]
            intent.putExtra("teamName", selectedText)
            startActivity(intent)
        }
    }

    //ListItemClickListenerPaとほぼ同じ、paLeagueがseLeagueになっただけ
    private inner class ListItemClickListenerSe: AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val intent = Intent(applicationContext, TeamDetailActivity::class.java)
            //positionは1から帰ってくるので"-1"する
            val selectedText = seLeague[position - 1]
            intent.putExtra("teamName", selectedText)
            startActivity(intent)
        }
    }
}
