package io.github.m_mememe.npb_stats_ref

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RankingTopRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val item: TextView = view.findViewById(R.id.item)
    val name: TextView = view.findViewById(R.id.name)
    val stats: TextView = view.findViewById(R.id.stats)
}