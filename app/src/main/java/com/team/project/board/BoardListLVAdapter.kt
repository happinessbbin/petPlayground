package com.team.project.board

import android.content.ContentValues
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.team.project.firebaseuser.UserModel
import com.team.project.utils.FBRef
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
        val content = view?.findViewById<TextView>(R.id.contentArea)
        val time = view?.findViewById<TextView>(R.id.timeArea)
        val image = view?.findViewById<ImageView>(R.id.getImageArea)
        val profile = view?.findViewById<ImageView>(R.id.profile)!!
        val name = view?.findViewById<TextView>(R.id.name)!!

        selectWriter(boardList[position].uid,view.context,name,profile)

        content!!.text = boardList[position].content
        time!!.text = boardList[position].time

        if (!boardList[position].image.equals("EMPTY")) {
            Glide.with(view!!.context)
                .load(boardList[position].image)
                .into(image!!)
        }

        return view!!
    }

    /***
     * @Service: selectUserInfo(uid : String) -  (해당) User 조회
     * @Param1 : String (uid)
     * @Description : 사용자의 uid로 Firebase users객체에 있는 해당 uid 사용자의 정보를 찾음
     ***/
    fun selectWriter(uid :String,context: Context,name:TextView,profile:ImageView) {
        Log.d(ContentValues.TAG, "SERVICE - selectWriter")

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // Firebase에 담긴 User를 UserModel 객체로 가져옴.
                val userModel = dataSnapshot.getValue(UserModel::class.java)
                Log.d(TAG,"anjdi?"+userModel?.userName)
                name.setText(userModel?.userName)

                // User Porfile 값이 "EMPTY" 가 아닐때만 프로필 셋팅
                if (!userModel?.profileImageUrl.equals("EMPTY")) {
                    Glide.with(context)
                        .load(userModel?.profileImageUrl)
                        .into(profile)
                }else{
                    Glide.with(context)
                        .load(R.drawable.profilede)
                        .into(profile)
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        // 파이어베이스에 users객체의 해당 uid에 해당 이벤트를 전달
        FBRef.userInfoRef.child(uid).addValueEventListener(postListener)
    }

}