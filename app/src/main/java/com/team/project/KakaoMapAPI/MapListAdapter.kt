package com.team.project.KakaoMapAPI

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team.project.R

class MapListAdapter (val itemList: ArrayList<MapListLayout>): RecyclerView.Adapter<MapListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.map_list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MapListAdapter.ViewHolder, position: Int) {
        holder.name.text = itemList[position].name
        holder.road.text = itemList[position].road
        holder.distance.text = itemList[position].distance
        holder.category.text = itemList[position].category
        holder.phone.text = itemList[position].phone



        // 아이템 클릭 이벤트
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_list_name)
        val road: TextView = itemView.findViewById(R.id.tv_list_road)
        val category: TextView = itemView.findViewById(R.id.tv_list_category)
        val distance: TextView = itemView.findViewById(R.id.tv_list_distance)
        val phone: TextView = itemView.findViewById(R.id.tv_list_phone)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener
}