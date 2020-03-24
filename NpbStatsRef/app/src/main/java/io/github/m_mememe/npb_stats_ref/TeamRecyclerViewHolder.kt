package io.github.m_mememe.npb_stats_ref

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TeamRecyclerViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

    val nameTextView: TextView = itemview.findViewById(R.id.name)
    val textView1: TextView = itemview.findViewById(R.id.data1)
    val textView2: TextView = itemview.findViewById(R.id.data2)
    val textView3: TextView = itemview.findViewById(R.id.data3)
    val textView4: TextView = itemview.findViewById(R.id.data4)
    val textView5: TextView = itemview.findViewById(R.id.data5)
    val textView6: TextView = itemview.findViewById(R.id.data6)
}