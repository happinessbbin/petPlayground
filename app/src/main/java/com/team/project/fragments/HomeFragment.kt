package com.team.project.fragments

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.team.project.R
import com.team.project.contentsList.BookmarkRVAdapter
import com.team.project.contentsList.ContentModel
import com.team.project.databinding.FragmentHomeBinding
import com.team.project.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.project.petfinder.banner.MyIntroPagerRecyclerAdapter
import com.project.petfinder.banner.PageItem
import com.team.project.MainActivity
import com.team.project.utils.FBAuth
import kotlinx.android.synthetic.main.activity_content_show.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.webView1
import kotlinx.android.synthetic.main.webview.view.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    lateinit var mainActivity: MainActivity

    private val TAG = HomeFragment::class.java.simpleName

    val bookmarkIdList = mutableListOf<String>()
    val items = ArrayList<ContentModel>()
    val itemKeyList = ArrayList<String>()

    // banner
    private var homeBannerHandler = HomeBannerHandler()
    // 인덱스값이 아이템 수의 절반, 딱 중간쯤에서 시작하도록 해 앞뒤 어디로 이동해도 무한대처럼 보이게 함
    private var bannerPosition = Int.MAX_VALUE/4
    // 1.5 초 간격으로 배너 페이지 넘어감
    private val intervalTime = 3500.toLong()

    // 배너 어댑터 초기화
    private lateinit var bannerRecyclerAdapter: MyIntroPagerRecyclerAdapter

    private var pageItemList = ArrayList<PageItem>()

    lateinit var rvAdapter : BookmarkRVAdapter

    // 배너 버튼 초기화
    lateinit var previous_btn: ImageView
    lateinit var next_btn: ImageView
    lateinit var my_intro_view_pager:ViewPager2
    lateinit var dots_indicator:com.tbuonomo.viewpagerdotsindicator.DotsIndicator


    var i=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var uid = FBAuth.getUid()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = getActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("HomeFragment", "onCreateView")

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        // 웹뷰
        clickWebView(binding.root)

        // banner
        banner(binding.root)

        binding.tipTap.setOnClickListener {
            Log.d("HomeFragment", "tipTap")
            it.findNavController().navigate(R.id.action_homeFragment_to_tipFragment)
        }

        binding.talkTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_homeFragment_to_talkFragment)

        }

        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_myInfoFragment)
        }


        return binding.root
    }

    private fun clickWebView(view: View){
        // 웹뷰 1
        val bundle = Bundle()

        setUrl(binding.webView1,"https://blog.naver.com/seomee1203/222223002871",bundle)
        setUrl(binding.category1,"https://m.holapet.com/place/pensions/region",bundle)
        setUrl(binding.category2,"https://m.holapet.com/place/category/4",bundle)
        setUrl(binding.category3,"https://m.holapet.com/place/category/11",bundle)
        setUrl(binding.category4,"https://m.holapet.com/place/category/2",bundle)
        setUrl(binding.category5,"https://m.holapet.com/place/category/3",bundle)
        setUrl(binding.category6,"https://m.holapet.com/place/category/14",bundle)
        setUrl(binding.category7,"https://m.holapet.com/place/category/6",bundle)
        setUrl(binding.category8,"https://m.holapet.com/place/category/5",bundle)

    }

    fun setUrl(button:ImageView, url:String,bundle:Bundle){
        button.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("url", url)
            it.findNavController().navigate(R.id.action_homeFragment_to_webViewFragment, bundle)
        }
    }

    // 배너
    fun banner(view:View){

        // 배너 사진 설정
        pageItemList.add(PageItem(R.drawable.ban1))
        pageItemList.add(PageItem(R.drawable.ban2))
        pageItemList.add(PageItem(R.drawable.ban3))
        pageItemList.add(PageItem(R.drawable.ban4))
        pageItemList.add(PageItem(R.drawable.ban5))
        pageItemList.add(PageItem(R.drawable.ban6))
        pageItemList.add(PageItem(R.drawable.ban7))
        pageItemList.add(PageItem(R.drawable.ban8))


        bannerRecyclerAdapter = MyIntroPagerRecyclerAdapter(pageItemList)

        binding.myIntroViewPager.apply {
            adapter = bannerRecyclerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                //이 banner드의 state 값으로 뷰페이저의 상태를 알 수 있음
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    when (state) {
                        //뷰페이저가 움직이는 중일 때 자동 스크롤 시작 함수 호출
                        ViewPager2.SCROLL_STATE_DRAGGING -> autoScrollStop()
                        //뷰페이저에서 손 뗐을 때, 뷰페이저가 멈춰있을 때 자동 스크롤 멈춤 함수 호출
                        ViewPager2.SCROLL_STATE_IDLE -> autoScrollStart(intervalTime)
                    }
                }
            })
        }

    }

    //배너 자동 스크롤 컨트롤하는 클래스
    private inner class HomeBannerHandler: Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            Log.d(TAG,"ba:"+bannerPosition)
            if(msg.what == 0){

                binding.myIntroViewPager.setCurrentItem(i++, true ) //다음 페이지로 이동
                autoScrollStart(intervalTime) //스크롤 킵고잉

                // 8개 넘었을경우 다시 초기화
                if(i > 8){ i = 0 }
            }
        }
    }


    //배너 자동 스크롤 시작하게 하는 함수
    private fun autoScrollStart(intervalTime: Long){
        homeBannerHandler.removeMessages(0) //이거 안하면 핸들러가 여러개로 계속 늘어남
        homeBannerHandler.sendEmptyMessageDelayed(0, intervalTime) //intervalTime만큼 반복해서 핸들러를 실행
    }

    //배너 자동 스크롤 멈추게 하는 함수
    private fun autoScrollStop(){
        homeBannerHandler.removeMessages(0) //핸들러 중지
    }


    //다른 화면으로 갔다가 돌아오면 배너 스크롤 다시 시작
    override fun onResume() {
        super.onResume()
        autoScrollStart(intervalTime)
    }

    //다른 화면을 보고 있는 동안에는 배너 스크롤 안함
    override fun onPause() {
        super.onPause()
        autoScrollStop()
    }
}