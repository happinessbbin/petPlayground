package com.team.project.fragments

import android.content.Context
import android.content.ContentValues.TAG

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.team.project.MainActivity
import com.team.project.R
import com.team.project.databinding.ActivityMyInfoBinding
import com.team.project.firebaseuser.UserModel
import  com.team.project.utils.FBRef

class MyInfoFragment : Fragment() {

    lateinit var uid: String
    lateinit var email: String

    lateinit var mainActivity: MainActivity

    private lateinit var binding: ActivityMyInfoBinding

    companion object {
        fun newInstance(): MyInfoFragment = MyInfoFragment()
    }

    /**
     * User 정보 객체 생성
     *
     * - UI적 부분은 onCreateView()가 실행되기 전 실행되야함.
     *             Fragment의 생명주기
     * onAttach() -> onCreate() -> onCreateView() ->
     *
     */

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = getActivity() as MainActivity

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /************* User 정보 객체 생성  **************/
        
         uid = Firebase.auth.uid.toString()

        // UI적 부분은 onCreateView()가 실행되기 전 실행되야함.
        selectUser(uid)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // binding 할당
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_my_info, container, false)

        /******** 버튼 클릭 이벤트 ********/


        // 메인바
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_myInfoFragment_to_homeFragment)
        }

        binding.tipTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_myInfoFragment_to_tipFragment)
        }

        binding.talkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_myInfoFragment_to_talkFragment)
        }

        
        // 비밀번호 변경 버튼
        binding.btnPwchange.setOnClickListener(View.OnClickListener {
            it.findNavController().navigate(R.id.action_myInfoFragment_to_myInfoChangePwFragment)

        })

        // 회원 탈퇴 버튼
        binding.btnWithdrawal.setOnClickListener(View.OnClickListener {
            it.findNavController().navigate(R.id.action_myInfoFragment_to_myInfoDeleteFragment)
        })


        // 로그아웃 버튼

        binding.btnLogout.setOnClickListener {

            Firebase.auth.signOut()

//            Toast.makeText(MainActivity, "로그아웃", Toast.LENGTH_LONG).show()

            it.findNavController().navigate(R.id.action_myInfoFragment_to_loginActivity)

        }
        /*******************************/

        return binding.root
    }

    /***
     * @Service: selectUserInfo(uid : String) -  (해당) User 조회
     * @Param1 : String (uid)
     * @Description : 사용자의 uid로 Firebase users객체에 있는 해당 uid 사용자의 정보를 찾음
     ***/
    fun selectUser(uid: String) {
        Log.d(TAG, "SERVICE - selectUser")

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // Firebase에 담긴 User를 UserModel 객체로 가져옴.
                val userModel = dataSnapshot.getValue(UserModel::class.java)

                /*********** UI Setting ************/
                binding.myName.setText(userModel?.userName)
                binding.inputPhoneNum.setText(userModel?.phone)
                binding.inputEmail.setText(userModel?.userEmail)

                // 이메일값 담기 (비밀번호 변경때 쓰기 위해서)

                mainActivity.email = userModel?.userEmail.toString()

                // User Porfile 값이 "EMPTY" 가 아닐때만 프로필 셋팅
                if (!userModel?.profileImageUrl.equals("EMPTY")) {
                    Glide.with(mainActivity)
                        .load(userModel?.profileImageUrl)
                        .into(binding.myProfile)
                }
                /*********************************/

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        // 파이어베이스에 users객체의 해당 uid에 해당 이벤트를 전달
        FBRef.userInfoRef.child(uid).addValueEventListener(postListener)
    }
}


