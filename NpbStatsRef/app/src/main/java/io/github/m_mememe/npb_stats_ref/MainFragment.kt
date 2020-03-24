package io.github.m_mememe.npb_stats_ref

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController


class MainFragment : Fragment() {
    val paNum = 6
    val seNum = 6

    private val leagueLogo = ItemManagement().leagueLogo
    private val leagueId = ItemManagement().leagueId
    private val teamList = ItemManagement().teamList
    private val statsTypeList = ItemManagement().statsTypeList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //セとパそれぞれでListViewを作成する
        val lvPaLeague = view.findViewById<ListView>(R.id.lvPaLeague)
        val adapterPa = LeagueAdapter(getContext(), R.layout.list_league, leagueLogo.subList(0, paNum), leagueId.subList(0, paNum))
        val headerPa = layoutInflater.inflate(R.layout.header_league, null)
        val headerPaText = headerPa.findViewById<TextView>(R.id.tvLeague)
        headerPa.setBackgroundColor(Color.RED)
        headerPaText.text = getString(R.string.pa_league)
        lvPaLeague.addHeaderView(headerPa, null, false)
        lvPaLeague.adapter = adapterPa
        lvPaLeague.onItemClickListener = ListItemClickListener()

        val lvSeLeague = view.findViewById<ListView>(R.id.lvSeLeague)
        val adapterSe = LeagueAdapter(getContext(), R.layout.list_league, leagueLogo.subList(paNum, paNum+seNum), leagueId.subList(paNum, paNum+seNum))
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

            val teamId = teamList[position+cBias-1]
            val statsType = statsTypeList[0] // 最初は打撃成績を表示

            val action = MainFragmentDirections.actionNavHomeToNavTeam(teamId, statsType)
            findNavController().navigate(action)

        }
    }
}
