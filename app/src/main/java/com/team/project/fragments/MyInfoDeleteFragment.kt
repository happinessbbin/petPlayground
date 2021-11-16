package com.team.project

import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.team.project.auth.LoginActivity
import com.team.project.databinding.ActivityMyInfoDeleteBinding
import com.team.project.utils.FBAuth
import com.team.project.utils.FBRef
import android.content.DialogInterface
import androidx.databinding.DataBindingUtil


class MyInfoDeleteFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var binding: ActivityMyInfoDeleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = getActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // binding 할당
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_my_info_delete, container, false)
        val view: View = binding.getRoot()

        binding.checkBox.setOnClickListener {

            if(binding.checkBox.isChecked) {
                // 확인 버튼 클릭 시 -> 내정보 화면으로 이동
                binding.btnSubmit.setBackgroundColor(Color.rgb(52,152,219))
                binding.btnSubmit.setOnClickListener {

                    // 알림창 - 정말 탈퇴 하시겠습니까?
                    var builder: AlertDialog.Builder = AlertDialog.Builder(context)

                    builder.setTitle("탈퇴하기")
                    builder.setMessage("정말 탈퇴 하시겠습니까?")
                    builder.setIcon(R.drawable.petfoot_yellow)

                    builder.setPositiveButton("예") { dialog, id ->
                        deleteUser(FBAuth.getUid())
                    }

                    builder.setNegativeButton("아니요") { dialog, id ->
                        dialog.dismiss()
                    }

                    val alertDialog:AlertDialog = builder.create()
                    alertDialog.show()
                }
            }else {
                binding.btnSubmit.setBackgroundColor(Color.rgb(124,124,123))
                binding.btnSubmit.setClickable(false)
                }
            }

        // 취소 버튼 클릭 시 -> 내정보 화면으로 이동
        binding.btnCancel.setOnClickListener(View.OnClickListener {
            it.findNavController().navigate(R.id.action_myInfoDeleteFragment_to_myInfoFragment)
        })

        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener (View.OnClickListener {
            it.findNavController().navigate(R.id.action_myInfoDeleteFragment_to_myInfoFragment)
        })

        return view
    }


    /*** deleteUser(uid :String) -  (해당) User 탈퇴
     *  Param1 : String (uid)
     ***/
    fun deleteUser(uid :String) {

        Log.d(ContentValues.TAG, "SERVICE - deleteUser")

        val user = Firebase.auth.currentUser!!

        val prePassword = binding.inputPw.text.toString()

        /*** 유효성 체크 ***/
        if(NullCheck(prePassword)) {
            Toast.makeText(mainActivity, "현재 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()

            return
        }

        if(!mainActivity.pass.equals(prePassword)){
            Toast.makeText(mainActivity, "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show()

            return
        }

        /*** FireBase에서 삭제 ***/
        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")

                    FBRef.userInfoRef.child(uid).removeValue()
                    Toast.makeText(mainActivity, "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show()

                }
            }

        //로그아웃처리
//        FirebaseAuth.getInstance().signOut()

        val intent = Intent(mainActivity, LoginActivity::class.java)
        startActivity(intent)

    }

    /**
     * @Service : NullCheck - null이나 "" 공백 체크
     * @return : Blooean (빈값이면 : true | 빈값이 아니면: false)
     */
    fun NullCheck(inputData : String):Boolean {
        return inputData.isEmpty() || inputData.equals("")
    }
}