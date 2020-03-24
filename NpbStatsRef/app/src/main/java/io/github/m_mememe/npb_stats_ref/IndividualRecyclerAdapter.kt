package io.github.m_mememe.npb_stats_ref

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class IndividualRecyclerAdapter(private val list:List<IndividualData>, private val listener: ListListener) : RecyclerView.Adapter<IndividualRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndividualRecyclerViewHolder {
        val individualView: View = LayoutInflater.from(parent.context).inflate(R.layout.list_individual, parent, false)
        return IndividualRecyclerViewHolder(individualView)
    }

    override fun onBindViewHolder(holder: IndividualRecyclerViewHolder, position: Int) {
        holder.individualItem1.text = list[position].item1
        holder.individualStats1.text = list[position].stats1
        holder.individualItem2.text = list[position].item2
        holder.individualStats2.text = list[position].stats2
        holder.individualItem3.text = list[position].item3
        holder.individualStats3.text = list[position].stats3
        holder.itemView.setOnClickListener {
            listener.onClickData(it, list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ListListener {
        fun onClickData(tappedView: View, dataForm: IndividualData)
    }

}