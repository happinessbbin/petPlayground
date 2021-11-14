package com.team.project.KakaoMapAPI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team.project.R

class MapListAdapter (val itemList: ArrayList<MapListModel>): RecyclerView.Adapter<MapListAdapter.ViewHolder>() {

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

       var keyword:String = itemList[position].keyword

        setImage(keyword,holder.itemView,holder.image)

        // 아이템 클릭 이벤트
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

        // 아이템 클릭 이벤트
        holder.image.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url", itemList[position].url)
            it.findNavController().navigate(R.id.action_mapFragment_to_webViewFragment, bundle)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_list_name)
        val road: TextView = itemView.findViewById(R.id.tv_list_road)
        val category: TextView = itemView.findViewById(R.id.tv_list_category)
        val distance: TextView = itemView.findViewById(R.id.tv_list_distance)
        val phone: TextView = itemView.findViewById(R.id.tv_list_phone)
        val image: ImageView = itemView.findViewById(R.id.image)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    fun setImage(keyword:String,view:View,image:ImageView){
        when (keyword) {
            "동물병원" -> {
                Glide.with(view)
                    .load(R.drawable.imagehospital)
                    .into(image)
            }
            "애견카페" ->  {
                Glide.with(view)
                    .load(R.drawable.petcoffe)
                    .into(image)
            }
            "애견식당" ->  {
                Glide.with(view)
                    .load(R.drawable.petfood)
                    .into(image)
            }
            "애견동반호텔" ->  {
                Glide.with(view)
                    .load(R.drawable.petcoffe)
                    .into(image)
            }
            "애견용품" ->  {
                Glide.with(view)
                    .load(R.drawable.petcoffe)
                    .into(image)
            }
        }
    }
    private lateinit var itemClickListener : OnItemClickListener
}