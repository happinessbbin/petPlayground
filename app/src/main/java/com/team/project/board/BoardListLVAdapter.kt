package com.team.project.board

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.team.project.R
import com.team.project.utils.FBAuth
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.database.*
import com.team.project.firebaseuser.UserModel
import com.team.project.utils.FBRef


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
        var flag = false
        view = LayoutInflater.from(parent?.context).inflate(R.layout.board_list_item, parent, false)

        val itemLinearLayoutView = view?.findViewById<LinearLayout>(R.id.itemView)
        val content = view?.findViewById<TextView>(R.id.contentArea)
        val time = view?.findViewById<TextView>(R.id.timeArea)
        val image = view?.findViewById<ImageView>(R.id.getImageArea)
        val profile = view?.findViewById<ImageView>(R.id.profile)!!
        val name = view?.findViewById<TextView>(R.id.name)!!
        val favorite = view?.findViewById<ImageView>(R.id.favorite)!!
        var favoriteCount =view?.findViewById<TextView>(R.id.favoriteCount)!!

        /*** 하트 증가,감소 UI 변경 ***/
        boardList[position].favorites.forEach { data ->
            if (data.key.equals(FBAuth.getUid())) {
                favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
        }

        favorite.setOnClickListener {

            FBRef.boardRef.child(boardList[position].boardUid)
                .runTransaction(object : Transaction.Handler {
                    override fun doTransaction(mutableData: MutableData): Transaction.Result {
                        val uid = FBAuth.getUid()
                        val p = mutableData.getValue(BoardModel::class.java)
                            ?: return Transaction.success(mutableData)

                        if (p.favorites.containsKey(uid)) {
                            favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                            // Unstar the post and remove self from stars
                            p.favoriteCount = p.favoriteCount - 1
                            favoriteCount!!.text = boardList[position].favoriteCount.toString()
                            p.favorites.remove(uid)

                        } else {
                            favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                            // Star the post and add self to stars
                            p.favoriteCount = p.favoriteCount + 1
                            favoriteCount!!.text = boardList[position].favoriteCount.toString()
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

                    }
                })
        }

        /*** favoriteCount 증가,감소 ***/
        favoriteCount!!.text = boardList[position].favoriteCount.toString()

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
