package io.github.m_mememe.npb_stats_ref

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class RecyclerAdapter(private val list:List<RowData>, private val listener: ListListener) : RecyclerView.Adapter<RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val rowView: View = LayoutInflater.from(parent.context).inflate(R.layout.list, parent, false)
        return RecyclerViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.nameTextView.text = list[position].name
        holder.textView1.text = list[position].handed
        holder.textView2.text = list[position].battingAverage
        holder.textView3.text = list[position].homeruns
        holder.textView4.text = list[position].runsBattedIn
        holder.textView5.text = list[position].OPS
        holder.itemView.setOnClickListener {
            listener.onClickData(it, list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ListListener {
        fun onClickData(tappedView: View, rowData: RowData)
    }

}