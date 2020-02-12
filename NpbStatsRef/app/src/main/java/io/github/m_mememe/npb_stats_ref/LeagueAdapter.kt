package io.github.m_mememe.npb_stats_ref

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class LeagueAdapter(context: Context, itemId: Int, logo: List<Int>, league: List<Int>) : BaseAdapter(){
    private val inflater = LayoutInflater.from(context)
    private val layoutId = itemId
    private val logoList = arrayOfNulls<Bitmap>(logo.size)
    private val leagueList = arrayOfNulls<String>(league.size)
    init{
        for(i in logoList.indices){
            logoList[i] = BitmapFactory.decodeResource(context.resources, logo[i])
        }
        for(i in leagueList.indices){
            leagueList[i] = context.getString(league[i])
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder = ViewHolder()
        val view = convertView ?: inflater.inflate(layoutId, null)

        holder.logo = view.findViewById(R.id.ivLogo)
        holder.league = view.findViewById(R.id.tvTeam)
        holder.logo.setImageBitmap(logoList[position])
        holder.league.text = leagueList[position]
        view.tag = holder
        return view
    }

    override fun getCount(): Int {
        return logoList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private inner class ViewHolder {
        lateinit var logo: ImageView
        lateinit var league: TextView
    }
}