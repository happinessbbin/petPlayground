package com.team.project.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.team.project.MainActivity
import com.team.project.R
import kotlinx.android.synthetic.main.activity_content_show.view.*
import android.webkit.WebSettings
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.team.project.databinding.ActivityMyInfoBinding
import com.team.project.databinding.FragmentTipBinding


class TipFragment : Fragment() {

    private var mWebView: WebView? = null
    private var mWebSettings : WebSettings? = null

    lateinit var mainActivity: MainActivity
    private lateinit var binding : FragmentTipBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        fun newInstance() : TipFragment = TipFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = getActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        // binding 할당
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tip, container, false)

        val url:String = arguments?.getString("url").toString()
        Log.d(TAG,"URL:"+url)

        // 웹뷰 시작
        // 웹뷰 시작

        binding.webView1.setWebViewClient(WebViewClient()) // 클릭시 새창 안뜨게

        mWebSettings =binding.webView1?.getSettings() //세부 세팅 등록

        mWebSettings?.setJavaScriptEnabled(true) // 웹페이지 자바스클비트 허용 여부
        mWebSettings?.setSupportMultipleWindows(false) // 새창 띄우기 허용 여부
        mWebSettings?.setJavaScriptCanOpenWindowsAutomatically(false) // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSettings?.setLoadWithOverviewMode(true) // 메타태그 허용 여부
        mWebSettings?.setUseWideViewPort(true) // 화면 사이즈 맞추기 허용 여부
        mWebSettings?.setSupportZoom(false) // 화면 줌 허용 여부
        mWebSettings?.setBuiltInZoomControls(false) // 화면 확대 축소 허용 여부
        mWebSettings?.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN) // 컨텐츠 사이즈 맞추기
        mWebSettings?.setCacheMode(WebSettings.LOAD_NO_CACHE) // 브라우저 캐시 허용 여부
        mWebSettings?.setDomStorageEnabled(true) // 로컬저장소 허용 여부

        binding.webView1?.loadUrl(url) // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작


        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_tipFragment_to_homeFragment)
        }

        binding.talkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_tipFragment_to_talkFragment)
        }

        binding.map.setOnClickListener {
            it.findNavController().navigate(R.id.action_tipFragment_to_mapFragment)
        }

        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_tipFragment_to_myInfoFragment)
        }

        return binding.root
    }


    fun showWebView(url:String){



    }




}