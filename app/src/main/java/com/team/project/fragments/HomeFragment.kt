package com.team.project.fragments

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
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.team.project.R
import com.team.project.contentsList.BookmarkRVAdapter
import com.team.project.contentsList.ContentModel
import com.team.project.databinding.FragmentHomeBinding
import com.project.petfinder.banner.MyIntroPagerRecyclerAdapter
import com.project.petfinder.banner.PageItem
import com.team.project.MainActivity
import com.team.project.utils.FBAuth
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
    private val intervalTime = 5000.toLong()

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
            val bundle = Bundle()
            bundle.putString("url", "https://tools.mypetlife.co.kr/")
            it.findNavController().navigate(R.id.action_homeFragment_to_tipFragment, bundle)

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

        setUrl(binding.webView0,"https://www.ban-life.com/content/list?t=01",bundle)
        setUrl(binding.webView1,"https://www.ban-life.com/content/view?id=5175",bundle)
        setUrl(binding.webView2,"https://www.ban-life.com/content/view?id=5156",bundle)
        setUrl(binding.webView3,"https://www.ban-life.com/content/view?id=5150",bundle)
        setUrl(binding.webView4,"https://www.ban-life.com/content/view?id=5165",bundle)


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
            bundle.putString("url", url)
            it.findNavController().navigate(R.id.action_homeFragment_to_webViewFragment, bundle)
        }
    }

    // 배너
    fun banner(view:View){

        // 배너 사진 설정
        pageItemList.add(PageItem(R.drawable.ban1,"https://www.ban-life.com/shop/products/9?utm_source=naver&utm_medium=organic&utm_campaign=2111_hanriver_yacht"))
        pageItemList.add(PageItem(R.drawable.ban2,"https://www.ban-life.com/shop/products/8?utm_source=naver&utm_medium=organic&utm_campaign=2110_cpferry"))

        pageItemList.add(PageItem(R.drawable.ban3,"https://www.ban-life.com/shop/products/7?utm_source=naver&utm_medium=organic&utm_campaign=2110_sl_hotel"))
        pageItemList.add(PageItem(R.drawable.ban4,"https://www.ban-life.com/content/view?id=4936"))
        pageItemList.add(PageItem(R.drawable.ban5,"https://www.ban-life.com/content/view?id=4945"))
        pageItemList.add(PageItem(R.drawable.ban6,"https://www.ban-life.com/content/view?id=5031"))
        pageItemList.add(PageItem(R.drawable.ban7,"https://www.ban-life.com/content/view?id=4526"))
        pageItemList.add(PageItem(R.drawable.ban8,"https://www.ban-life.com/shop/products/5"))

        bannerRecyclerAdapter = MyIntroPagerRecyclerAdapter(pageItemList)


        binding.myIntroViewPager.apply {
            adapter = bannerRecyclerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                //이 banner의 state 값으로 뷰페이저의 상태를 알 수 있음
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