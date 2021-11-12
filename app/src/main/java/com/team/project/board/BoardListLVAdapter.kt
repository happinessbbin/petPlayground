package com.team.project.board

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.team.project.R
import com.team.project.utils.FBAuth
import java.util.ArrayList
import android.graphics.drawable.Drawable
import androidx.annotation.Nullable
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule

import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule

import com.bumptech.glide.request.RequestListener
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit


class BoardListLVAdapter(val boardList : MutableList<BoardModel>) : BaseAdapter() {

    override fun getCount(): Int {
        return boardList.size
    }

    override fun getItem(position: Int): Any {
        return boardList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView

        view = LayoutInflater.from(parent?.context).inflate(R.layout.board_list_item, parent, false)

        val itemLinearLayoutView = view?.findViewById<LinearLayout>(R.id.itemView)
        val title = view?.findViewById<TextView>(R.id.titleArea)
        val content = view?.findViewById<TextView>(R.id.contentArea)
        val time = view?.findViewById<TextView>(R.id.timeArea)
        val image = view?.findViewById<ImageView>(R.id.getImageArea)

//        if(boardList[position].uid.equals(FBAuth.getUid())) {
//            itemLinearLayoutView?.setBackgroundColor(Color.parseColor("#ffa500"))
//        }

        title!!.text = boardList[position].title
        content!!.text = boardList[position].content
        time!!.text = boardList[position].time

        Log.d(TAG,"image:??????????????"+boardList[position].image)
        if (!boardList[position].image.equals("EMPTY")) {
            Glide.with(view!!.context)
                .load(boardList[position].image)
                .into(image!!)
        }


        Log.d(TAG,"image:11111111111111"+boardList[position].image)

        return view!!
    }

}