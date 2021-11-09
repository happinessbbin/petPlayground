package com.project.petfinder.banner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.project.R

class MyIntroPagerRecyclerAdapter(private var pageList: ArrayList<PageItem>) : RecyclerView.Adapter<MyIntroPagerViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyIntroPagerViewHolder {
        return MyIntroPagerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.banner, parent, false))
    }

    override fun getItemCount(): Int {
        return pageList.size
    }

    override fun onBindViewHolder(holder: MyIntroPagerViewHolder, position: Int) {
        holder.bindWithView(pageList[position])
    }

}