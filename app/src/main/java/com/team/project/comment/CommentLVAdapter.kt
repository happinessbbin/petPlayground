package com.team.project.comment

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.team.project.MainActivity
import com.team.project.R


class CommentLVAdapter(val commentList : MutableList<CommentModel>) : BaseAdapter() {

    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(position: Int): Any {
        return commentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.comment_list_item, parent, false)
        }

        val title = view?.findViewById<TextView>(R.id.titleArea)
        val time = view?.findViewById<TextView>(R.id.timeArea)
        val name = view?.findViewById<TextView>(R.id.name)
        val profile = view?.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.profile)

        title!!.text = commentList[position].commentTitle
        time!!.text = commentList[position].commentCreatedTime
        name!!.text = commentList[position].userName


        if (!commentList[position].profile.equals("EMPTY")) {
            Glide.with(view!!.context)
                .load(commentList[position].profile)
                .into(profile!!)
        }

        return view!!
    }

}