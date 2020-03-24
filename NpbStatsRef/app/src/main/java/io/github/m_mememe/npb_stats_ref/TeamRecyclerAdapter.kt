package io.github.m_mememe.npb_stats_ref

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class TeamRecyclerAdapter(private val list:List<TeamData>, private val listener: ListListener) : RecyclerView.Adapter<TeamRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamRecyclerViewHolder {
        val rowView: View = LayoutInflater.from(parent.context).inflate(R.layout.list, parent, false)
        return TeamRecyclerViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: TeamRecyclerViewHolder, position: Int) {
        holder.nameTextView.text = list[position].name
        holder.textView1.text = list[position].data1
        holder.textView2.text = list[position].data2
        holder.textView3.text = list[position].data3
        holder.textView4.text = list[position].data4
        holder.textView5.text = list[position].data5
        holder.textView6.text = list[position].data6
        holder.itemView.setOnClickListener {
            listener.onClickData(it, list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ListListener {
        fun onClickData(tappedView: View, rowData: TeamData)
    }

}