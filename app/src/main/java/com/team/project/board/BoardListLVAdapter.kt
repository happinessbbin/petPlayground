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
import android.os.Build
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule

import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule

import com.bumptech.glide.request.RequestListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.team.project.contentsList.ContentListActivity
import com.team.project.databinding.FragmentTalkBinding
import com.team.project.firebaseuser.UserModel
import com.team.project.utils.FBRef
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit


class BoardListLVAdapter(val boardList: MutableList<BoardModel>) : BaseAdapter() {


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
        val favorite = view?.findViewById<ImageView>(R.id.favorite)!!
        val favoriteCount = view?.findViewById<TextView>(R.id.favoriteCount)!!


        favorite.setOnClickListener {

            FBRef.boardRef.child(boardList[position].boardUid)
                .runTransaction(object : Transaction.Handler {
                    override fun doTransaction(mutableData: MutableData): Transaction.Result {
                        val uid = FBAuth.getUid()
                        val p = mutableData.getValue(BoardModel::class.java)
                            ?: return Transaction.success(mutableData)

                        if (p.favorites.containsKey(uid)) {
                            // Unstar the post and remove self from stars
                            p.favoriteCount = p.favoriteCount - 1
                            p.favorites.remove(uid)
                        } else {
                            // Star the post and add self to stars
                            p.favoriteCount = p.favoriteCount + 1
                            p.favorites[uid] = true
                        }

                        // Set value and report transaction success
                        mutableData.value = p
                        return Transaction.success(mutableData)
                    }

                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun onComplete(
                        databaseError: DatabaseError?,
                        committed: Boolean,
                        currentData: DataSnapshot?
                    ) {
                        Log.d(TAG, "요기~~~" + currentData?.child("favorites")?.getValue())

                        // Transaction completed
//                        if(currentData.) {
//
//                        }else {
//
//                        }
                    }


                    // 하트 누른 상태이면
//                    if(contentDTOs!![position].favorites.containsKey(uid)){
//                        //This is like status
//                        viewholder.detailviewitem_favorite_imageview.setImageResource(R.drawable.ic_favorite)
//                    }else{
//                        //This is unlike status
//                        viewholder.detailviewitem_favorite_imageview.setImageResource(R.drawable.ic_favorite_border)
//                    }

        })
    }

    selectWriter(boardList[position].uid, view.context, name, profile)

    content!!.text = boardList[position].content
    time!!.text = boardList[position].time

    if (!boardList[position].image.equals("EMPTY"))
    {
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
fun selectWriter(uid: String, context: Context, name: TextView, profile: ImageView) {
    Log.d(ContentValues.TAG, "SERVICE - selectWriter")

    val postListener = object : ValueEventListener {

        override fun onDataChange(dataSnapshot: DataSnapshot) {

            // Firebase에 담긴 User를 UserModel 객체로 가져옴.
            val userModel = dataSnapshot.getValue(UserModel::class.java)
            Log.d(TAG, "anjdi?" + userModel?.userName)
            name.setText(userModel?.userName)

            // User Porfile 값이 "EMPTY" 가 아닐때만 프로필 셋팅
            if (!userModel?.profileImageUrl.equals("EMPTY")) {
                Glide.with(context)
                    .load(userModel?.profileImageUrl)
                    .into(profile)
            } else {
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
