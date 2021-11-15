package com.team.project.KakaoMapAPI

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
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
    var keyword:String ?= null
    override fun onBindViewHolder(holder: MapListAdapter.ViewHolder, position: Int) {

         keyword = itemList[position].keyword
        var code:String = itemList[position].typoeCode
        Log.d(TAG,"혹시111"+keyword)
        setImage(code,holder.itemView,holder.image)

        holder.name.text = itemList[position].name
        holder.road.text = itemList[position].road
        holder.distance.text = itemList[position].distance
        holder.category.text = itemList[position].category
        holder.phone.text = itemList[position].phone



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
        val image: ImageView = itemView.findViewById(R.id.tv_image)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    fun setImage(code:String,view:View,image:ImageView){

        Log.d(TAG,"혹시222"+code)
        Log.d(TAG,"혹시222"+keyword)

        when (code) {
            "HP8" -> {
                Glide.with(view)
                    .load(R.drawable.pethospital)
                    .into(image)
            }
            "CE7" ->  {
                Glide.with(view)
                    .load(R.drawable.petcafe)
                    .into(image)
            }
            "FD6" ->  {
                Glide.with(view)
                    .load(R.drawable.petfood)
                    .into(image)
            }
            "AD5" ->  {
                Glide.with(view)
                    .load(R.drawable.pethotel)
                    .into(image)
            }
//            "애견용품" ->  {
//                Glide.with(view)
//                    .load(R.drawable.petmd)
//                    .into(image)
//            }
            else ->  {
                Glide.with(view)
                    .load(R.drawable.allimage)
                    .into(image)
            }
        }
    }
    private lateinit var itemClickListener : OnItemClickListener
}