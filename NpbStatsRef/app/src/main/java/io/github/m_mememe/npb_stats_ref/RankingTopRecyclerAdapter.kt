package io.github.m_mememe.npb_stats_ref

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class RankingTopRecyclerAdapter(private val list:List<RankingTopData>, private val listener: ListListener) : RecyclerView.Adapter<RankingTopRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingTopRecyclerViewHolder {
        val rankingTopView: View = LayoutInflater.from(parent.context).inflate(R.layout.list_ranking_top, parent, false)
        return RankingTopRecyclerViewHolder(rankingTopView)
    }

    override fun onBindViewHolder(holder: RankingTopRecyclerViewHolder, position: Int) {
        holder.item.text = list[position].item
        holder.name.text = list[position].name
        holder.stats.text = list[position].stats
        holder.itemView.setOnClickListener {
            listener.onClickData(it, list[position], position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ListListener {
        fun onClickData(tappedView: View, data: RankingTopData, position: Int)
    }

}