package com.team.project.fragments

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.team.project.MainActivity
import com.team.project.R
import com.team.project.databinding.ActivityMyInfoNfcBinding
import com.team.project.utils.FBAuth

class MyInfoNfcFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var binding: ActivityMyInfoNfcBinding

    companion object {
        fun newInstance(): MyInfoNfcFragment = MyInfoNfcFragment()
    }

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
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_my_info_nfc, container, false)
        val view: View = binding.getRoot()
        //binding.폰넘버.text.equals("") || isEmpty()
        Log.d(TAG, "무엇이 나올까~?:" + binding.phoneNum.text)

        onClick(binding.phoneNum)

        //  binding.btnSubmit.setBackgroundColor(Color.rgb(52,152,219))
        binding.btnSubmit.setOnClickListener {


                Log.d(TAG, "번호 나올까~?:" + binding.phoneNum.text)

                // 알림창 - 태그 등록하기
                var builder: AlertDialog.Builder = AlertDialog.Builder(context)

                builder.setTitle("태그 등록하기")
                builder.setMessage("스마트폰을 태그에 접촉하세요.")
                builder.setIcon(R.drawable.ic_baseline_nfc_24)

                builder.setPositiveButton("예") { dialog, id ->
                    dialog.dismiss()
                }

                builder.setNegativeButton("아니요") { dialog, id ->
                    dialog.dismiss()
                }

                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()

        }

        // 취소 버튼 클릭 시 -> 내정보 화면으로 이동
        binding.btnCancel.setOnClickListener(View.OnClickListener {
            it.findNavController().navigate(R.id.action_myInfoNfcFragment_to_myInfoFragment)
        })

        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener (View.OnClickListener {
            it.findNavController().navigate(R.id.action_myInfoNfcFragment_to_myInfoFragment)
        })

        return view
    }


    /**
     * onClick - 로그인 클릭 안먹히게
     */
    fun onClick(inputText: EditText){
        //문자입력해야만 버튼이 활성화됨
        inputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {
                setColorAndAble(editable.length == 11)
            }
        })
        return
    }

    /**
     * setColorAndAble - 색상 변경 및 버튼 안먹히게
     */
    fun setColorAndAble(flag : Boolean){
        if(flag){
            binding.btnSubmit.setBackgroundColor(Color.rgb(52,152,219))
            binding.btnSubmit.setClickable(true)
            return
        }else{
            binding.btnSubmit.setClickable(false)
            binding.btnSubmit.setBackgroundColor(Color.rgb(153,153,153))
            return
        }

    }

    /**
     * @Service : NullCheck - null이나 "" 공백 체크
     * @return : Blooean (빈값이면 : true | 빈값이 아니면: false)
     */
    fun NullCheck(inputData : String):Boolean {
        return inputData.isEmpty() || inputData.equals("")
    }

}