package io.github.m_mememe.npb_stats_ref

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

    val nameTextView: TextView = itemview.findViewById(R.id.name)
    val textView1: TextView = itemview.findViewById(R.id.handed)
    val textView2: TextView = itemview.findViewById(R.id.battingAverage)
    val textView3: TextView = itemview.findViewById(R.id.homeruns)
    val textView4: TextView = itemview.findViewById(R.id.runsBattedIn)
    val textView5: TextView = itemview.findViewById(R.id.OPS)
}