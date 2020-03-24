package io.github.m_mememe.npb_stats_ref

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class RankingListRecyclerAdapter(private val list:List<RankingListData>, private val listener: ListListener) : RecyclerView.Adapter<RankingListRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingListRecyclerViewHolder {
        val rankingListView: View = LayoutInflater.from(parent.context).inflate(R.layout.list_ranking_list, parent, false)
        return RankingListRecyclerViewHolder(rankingListView)
    }

    override fun onBindViewHolder(holder: RankingListRecyclerViewHolder, position: Int) {
        holder.order.text = list[position].order
        holder.team.text = list[position].team
        holder.name.text = list[position].name
        holder.stats.text = list[position].stats
        holder.itemView.setOnClickListener {
            listener.onClickData(it, list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ListListener {
        fun onClickData(tappedView: View, data: RankingListData)
    }

}