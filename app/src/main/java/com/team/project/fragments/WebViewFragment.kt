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




class WebViewFragment : Fragment() {

    private var mWebView: WebView? = null
    private var mWebSettings : WebSettings? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        fun newInstance() : WebViewFragment = WebViewFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.webview, container, false)

        val url:String = arguments?.getString("url").toString()
        Log.d(TAG,"URL:"+url)

        // 웹뷰 시작
        // 웹뷰 시작
        mWebView = view.findViewById<WebView>(R.id.webView1)

        mWebView?.setWebViewClient(WebViewClient()) // 클릭시 새창 안뜨게

        mWebSettings = mWebView?.getSettings() //세부 세팅 등록

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

        mWebView?.loadUrl(url) // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작



        return view
    }


    fun showWebView(url:String){



    }
}