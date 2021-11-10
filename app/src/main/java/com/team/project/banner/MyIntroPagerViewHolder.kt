package com.project.petfinder.banner


import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.team.project.R
import kotlinx.android.synthetic.main.banner.view.*

class MyIntroPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val itemBg = itemView.pager_item_bg

    fun bindWithView(pageItem: PageItem){

        itemView.setOnClickListener {
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                val bundle = Bundle()
                bundle.putString("url", pageItem.url)
                it.findNavController().navigate(R.id.action_homeFragment_to_webViewFragment, bundle)
            }
        }

        itemBg.setBackgroundResource(pageItem.imageSrc)
    }
}