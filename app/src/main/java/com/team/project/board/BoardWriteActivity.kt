package com.team.project.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.team.project.R
import com.team.project.databinding.ActivityBoardWriteBinding
import com.team.project.utils.FBAuth
import com.team.project.utils.FBRef
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardWriteBinding

    private val TAG = BoardWriteActivity::class.java.simpleName

    private var isImageUpload = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        binding.writeBtn.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {

            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            Log.d(TAG, title)
            Log.d(TAG, content)

            // 파이어베이스 store에 이미지를 저장하고 싶습니다
            // 만약에 내가 게시글을 클릭했을 때, 게시글에 대한 정보를 받아와야 하는데
            // 이미지 이름에 대한 정보를 모르기 때문에
            // 이미지 이름을 문서의 key값으로 해줘서 이미지에 대한 정보를 찾기 쉽게 해놓음.

            val key = FBRef.boardRef.push().key.toString()

//                imageUpload(key)

                FBRef.boardRef
                    .child(key)
                    .setValue(BoardModel(title, content, uid, time,imageUpload(key)))
                Toast.makeText(this@BoardWriteActivity, "게시글 입력 완료", Toast.LENGTH_LONG).show()

                finish()

            }
        }

        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }

    }

    suspend fun imageUpload(key : String) : String{
        // Get the data from an ImageView as bytes
        Log.d(TAG,"imageUpload111111111:")
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

        var url = "EMPTY"

        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.e("firebase", "Error getting data", it)
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...

            val storageReference = Firebase.storage.reference.child(key + ".png")
            Log.i("firebase", "Got value111 ${taskSnapshot.uploadSessionUri}")

            Log.i("firebase", "Got value222 ${storage.getReferenceFromUrl(taskSnapshot.uploadSessionUri.toString())}")
            url = taskSnapshot.uploadSessionUri.toString()

            Log.d(TAG,"22222.33333333333:"+url)
        }

        delay(1500L)


        Log.d(TAG,"???:"+uploadTask)
        Log.d(TAG,"???:"+mountainsRef.root)

        return url

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100) {
            binding.imageArea.setImageURI(data?.data)
        }

    }
}