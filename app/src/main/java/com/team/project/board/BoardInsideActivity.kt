package com.team.project.board

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.team.project.R
import com.team.project.comment.CommentLVAdapter
import com.team.project.comment.CommentModel
import com.team.project.databinding.ActivityBoardInsideBinding
import com.team.project.utils.FBAuth
import com.team.project.utils.FBRef
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.team.project.firebaseuser.UserModel
import kotlinx.coroutines.*
import java.lang.Exception

class BoardInsideActivity : AppCompatActivity() {

    private val TAG = BoardInsideActivity::class.java.simpleName

    private lateinit var binding : ActivityBoardInsideBinding
    lateinit var uid :String
    private lateinit var key:String

    private val commentDataList = mutableListOf<CommentModel>()

    private lateinit var commentAdapter : CommentLVAdapter

    private lateinit var userUid:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)



        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }

        // 두번째 방법
        key = intent.getStringExtra("key").toString()

        CoroutineScope(Dispatchers.Main).launch {
            val job1 = CoroutineScope(Dispatchers.Default).async {
                getBoardData(key)
                getImageData(key)
            }.await()

            val job2 = CoroutineScope(Dispatchers.Default).async {
                delay(100)
                selectWriter(userUid,this@BoardInsideActivity)

            }.await()
        }


        binding.commentBtn.setOnClickListener {
            insertComment(key,this)
        }

        getCommentData(key)
        commentAdapter = CommentLVAdapter(commentDataList)

        binding.commentLV.adapter = commentAdapter



    }


    fun getCommentData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                }
                Log.d(TAG,"리스트111111111111111:"+commentDataList)

                commentAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)


    }

    fun insertComment(key : String,context: Context){

        var name:String ?= null
        var profile :String ?= null

        CoroutineScope(Dispatchers.Main).launch {
            val job1 = CoroutineScope(Dispatchers.Default).async {

                val postListener = object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        // Firebase에 담긴 User를 UserModel 객체로 가져옴.
                        val userModel = dataSnapshot.getValue(UserModel::class.java)
                        name = userModel?.userName
                        profile= userModel?.profileImageUrl

                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                    }
                }

                // 파이어베이스에 users객체의 해당 uid에 해당 이벤트를 전달
                FBRef.userInfoRef.child(userUid).addValueEventListener(postListener)
            }.await()

            val job2 = CoroutineScope(Dispatchers.Default).async {
                // comment
                //   - BoardKey
                //        - CommentKey
                //            - CommentData
                //            - CommentData
                //            - CommentData
                FBRef.commentRef
                    .child(key)
                    .push()
                    .setValue(
                        CommentModel(
                            binding.commentArea.text.toString(),
                            FBAuth.getTime(),
                            name!!,
                            profile!!

                        )
                    )

//                Toast.makeText(this@BoardInsideActivity, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
                binding.commentArea.setText("")
            }.await()
        }






    }

    private fun showDialog(){

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            Toast.makeText(this, "수정 버튼을 눌렀습니다", Toast.LENGTH_LONG).show()

            val intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("key",key)
            startActivity(intent)
        }

        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {

            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this, "삭제완료", Toast.LENGTH_LONG).show()
            finish()

        }



    }

    private fun getImageData(key : String){

        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.getImageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful) {

                Log.d(TAG,"도데체..:"+task.result)
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)

            } else {

                binding.getImageArea.isVisible = false
            }
        })
    }


    private fun getBoardData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    Log.d(TAG, dataModel!!.title)

                    binding.titleArea.text = dataModel!!.title
                    binding.textArea.text = dataModel!!.content
                    binding.timeArea.text = dataModel!!.time

                    // 게시판 글쓴 사용자
                    userUid = dataModel.uid

                    val myUid = FBAuth.getUid()
                    val writerUid = dataModel.uid


                    if(myUid.equals(writerUid)){
                        Log.d(TAG, "내가 쓴 글")
                        binding.boardSettingIcon.isVisible = true
                    } else {
                        Log.d(TAG, "내가 쓴 글 아님")
                    }

                } catch (e : Exception){

                    Log.d(TAG, "삭제완료")

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

    /***
     * @Service: selectUserInfo(uid : String) -  (해당) User 조회
     * @Param1 : String (uid)
     * @Description : 사용자의 uid로 Firebase users객체에 있는 해당 uid 사용자의 정보를 찾음
     ***/
   fun selectWriter(uid :String,context: Context) {
        Log.d(ContentValues.TAG, "SERVICE - selectWriter")

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // Firebase에 담긴 User를 UserModel 객체로 가져옴.
                val userModel = dataSnapshot.getValue(UserModel::class.java)
                binding.name.setText(userModel?.userName)

                // User Porfile 값이 "EMPTY" 가 아닐때만 프로필 셋팅
                if (!userModel?.profileImageUrl.equals("EMPTY")) {
                    Glide.with(context)
                        .load(userModel?.profileImageUrl)
                        .into(binding.myProfile)
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        // 파이어베이스에 users객체의 해당 uid에 해당 이벤트를 전달
        FBRef.userInfoRef.child(userUid).addValueEventListener(postListener)
    }

}