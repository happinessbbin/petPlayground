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
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
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

        var view: View = inflater.inflate(R.layout.fragment_home, container, false)

        // banner
        banner(view)

        binding.tipTap.setOnClickListener {
            Log.d("HomeFragment", "tipTap")
            it.findNavController().navigate(R.id.action_homeFragment_to_tipFragment)
        }

        binding.talkTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_homeFragment_to_talkFragment)

        }

//        binding.bookmarkTap.setOnClickListener {
//            it.findNavController().navigate(R.id.action_homeFragment_to_bookmarkFragment)
//        }

        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_storeFragment)
        }

        rvAdapter = BookmarkRVAdapter(requireContext(), items, itemKeyList, bookmarkIdList)

        val rv : RecyclerView = binding.mainRV
        rv.adapter = rvAdapter

        rv.layoutManager = GridLayoutManager(requireContext(), 2)

        getCategoryData()

        return binding.root
    }

    private fun getCategoryData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(ContentModel::class.java)

                    items.add(item!!)
                    itemKeyList.add(dataModel.key.toString())


                }
                rvAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.category1.addValueEventListener(postListener)
        FBRef.category2.addValueEventListener(postListener)

    }

    // 배너
    fun banner(view:View){


        // Pre Button
//        previous_btn.setOnClickListener {
//            Log.d(ContentValues.TAG, "MainActivity - 이전 버튼 클릭")
//
//            my_intro_view_pager.currentItem = my_intro_view_pager.currentItem - 1
//        }
//        // Next Button
//        next_btn.setOnClickListener {
//            Log.d(ContentValues.TAG, "MainActivity - 다음 버튼 클릭")
//            my_intro_view_pager.currentItem = my_intro_view_pager.currentItem + 1
//        }

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