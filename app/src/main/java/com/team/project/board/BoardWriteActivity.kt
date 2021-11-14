package com.team.project.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Tasks
import com.team.project.R
import com.team.project.databinding.ActivityBoardWriteBinding
import com.team.project.utils.FBAuth
import com.team.project.utils.FBRef
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardWriteBinding

    private val TAG = BoardWriteActivity::class.java.simpleName
    private var isImageUpload = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        binding.writeBtn.setOnClickListener {

            val key = FBRef.boardRef.push().key.toString()
            uploadImage(key)

        }

        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }

    }


    /**
     * @Service - uploadImage
     * @param: String
     * @return
     */
     fun uploadImage(key: String)  {

         /*** 메모리 데이터에서 업로드 ***/
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(key + ".png")

        val imageView = binding.imageArea
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)

         /*** 다운로드 URL 가져오기 ***/
        val urlTask = uploadTask.continueWithTask { task ->
            mountainsRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                /*** 보드 생성 ***/
                val title = binding.titleArea.text.toString()
                Log.d(TAG,"보드 uid?"+taskId)
                Log.d(TAG,"보드 uid?"+task)
                val content = binding.contentArea.text.toString()
                val uid = FBAuth.getUid()
                val time = FBAuth.getTime()
                val boardUid = key

                FBRef.boardRef
                    .child(key)
                    .setValue(BoardModel(title, content, uid, time,task.result.toString(), boardUid))

                finish()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100) {
            binding.imageArea.setImageURI(data?.data)
        }

    }
}