package com.team.project.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.team.project.MainActivity
import com.team.project.R
import com.team.project.databinding.ActivityMyInfoNfcBinding

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

}