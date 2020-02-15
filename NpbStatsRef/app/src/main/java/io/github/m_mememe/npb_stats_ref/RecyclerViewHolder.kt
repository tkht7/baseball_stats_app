package io.github.m_mememe.npb_stats_ref

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

    val nameTextView: TextView = itemview.findViewById(R.id.nameTextView)
    val textView1: TextView = itemview.findViewById(R.id.TextView1)
    val textView2: TextView = itemview.findViewById(R.id.TextView2)
    val textView3: TextView = itemview.findViewById(R.id.TextView3)
    val textView4: TextView = itemview.findViewById(R.id.TextView4)
    val textView5: TextView = itemview.findViewById(R.id.TextView5)
}