package com.team.project.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import com.team.project.R
import com.team.project.CalendarTodolist
import com.team.project.contentsList.ContentListActivity
import com.team.project.databinding.FragmentTipBinding
import kotlinx.android.synthetic.main.fragment_tip.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class TipFragment : Fragment() {

    private lateinit var binding : FragmentTipBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*        btn_calendar_write.setOnClickListener {
            try {
                //TODO:프래그먼트에선 openFileInput이 안되서 액티비티로 해야하나
                var inFs : FileInputStream = openFileInput("file.txt")
                var txt = ByteArray(30)
                inFs.read(txt)
                var str = txt.toString(Charsets.UTF_8)
                Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
                inFs.close()
            } catch (e : IOException) {
                Toast.makeText(applicationContext, "파일 없음", Toast.LENGTH_SHORT).show()
            }
        }*/

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tip, container, false)

        binding.btnSelectDate.setOnClickListener {

//            val intent = Intent(context, ContentListActivity::class.java)
//            intent.putExtra("category", "category1")
//            startActivity(intent)
            // create new instance of DatePickerFragment
            val datePickerFragment = DatePickerDiaryFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager

            // we have to implement setFragmentResultListener
            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ) { resultKey, bundle ->
                if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    tvSelectedDate.text = date
                }
            }

            // show
            datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
        }



        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_tipFragment_to_homeFragment)
        }

        binding.talkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_tipFragment_to_talkFragment)
        }

        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_tipFragment_to_myInfoFragment)
        }


        binding.btnCalendarWrite.setOnClickListener{
            val intent = Intent(context, CalendarTodolist::class.java)
            intent.putExtra("todolist", "todolist")
            startActivity(intent)
        }
/*        binding.btnCalendarWrite.setOnClickListener{
            val intent = Intent(context, ContentListActivity::class.java)
            intent.putExtra("todolist", "todolist")
            startActivity(intent)
        }//새로 액티비티 열려고 했는데 실패*/

        return binding.root
    }


}