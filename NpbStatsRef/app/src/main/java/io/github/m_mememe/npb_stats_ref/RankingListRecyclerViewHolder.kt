package io.github.m_mememe.npb_stats_ref

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RankingListRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val order: TextView = view.findViewById(R.id.order)
    val team: TextView = view.findViewById(R.id.teamName)
    val name: TextView = view.findViewById(R.id.name)
    val stats: TextView = view.findViewById(R.id.stats)
}