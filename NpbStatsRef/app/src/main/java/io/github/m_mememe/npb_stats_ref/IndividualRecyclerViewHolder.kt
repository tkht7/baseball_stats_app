package io.github.m_mememe.npb_stats_ref

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IndividualRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val individualItem1: TextView = view.findViewById(R.id.individualItem1)
    val individualStats1: TextView = view.findViewById(R.id.individualStats1)
    val individualItem2: TextView = view.findViewById(R.id.individualItem2)
    val individualStats2: TextView = view.findViewById(R.id.individualStats2)
    val individualItem3: TextView = view.findViewById(R.id.individualItem3)
    val individualStats3: TextView = view.findViewById(R.id.individualStats3)
}