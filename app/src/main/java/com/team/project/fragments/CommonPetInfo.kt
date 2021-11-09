package com.team.project.fragments

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.team.project.Join_User_PetInfo_Activity
import com.team.project.R
import com.team.project.utils.FBRef
import java.util.*
import kotlin.collections.ArrayList

class CommonPetInfo  {

//    lateinit var join_User_PetInfo_Activity: Join_User_PetInfo_Activity
//    lateinit var petInfoModifyFragment: PetInfoModifyFragment
//    lateinit var petInfoRegisterFragment: PetInfoRegisterFragment
//
//    private lateinit var dialogFragment: DialogFragment
//
//    companion object {
//        fun newInstance(): CommonPetInfo = CommonPetInfo()
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//
//        join_User_PetInfo_Activity = context as Join_User_PetInfo_Activity
//        petInfoModifyFragment = context as PetInfoModifyFragment
//        petInfoRegisterFragment = context as PetInfoRegisterFragment
//    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        var view: View = inflater.inflate(R.layout.common_pet_info, container, false)
//    }

    fun calendar(context: Context, view: View) {

        var calendar: Calendar = Calendar.getInstance()
        var year: Int = calendar.get(Calendar.YEAR)
        var month: Int = calendar.get(Calendar.MONTH)
        var day: Int = calendar.get(Calendar.DAY_OF_MONTH)

        var btn_calender = view?.findViewById(R.id.btn_calender) as Button
        var tv_pet_brith = view?.findViewById(R.id.tv_pet_brith) as TextView

        btn_calender.setOnClickListener(View.OnClickListener {
            val Cal = Calendar.getInstance()
            day = Cal[Calendar.DATE]
            month = Cal[Calendar.MONDAY]
            year = Cal[Calendar.YEAR]
            val datePickerDialog = DatePickerDialog(
                context, android.R.style.ThemeOverlay_Material_Dialog,
                { view, year, month, dayOfMonth ->
                    tv_pet_brith.setText("$year-$month-$dayOfMonth")
                },
                year, month + 1, day
            )
            datePickerDialog.show()
        })
    }

    fun spinner(context: Context, view: View) {

        // Spinner xml 연결
        var petKind = view.findViewById<Spinner>(R.id.inputPetKind)
        var petBreed = view.findViewById<Spinner>(R.id.inputPetBreed)

        // ArrayList 만들기
        var spinnerList: ArrayList<String> = ArrayList()
        var spinnerDog: ArrayList<String> = ArrayList()
        var spinnerCat: ArrayList<String> = ArrayList()

        // 어댑터 생성
        var adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, spinnerList!!)
        var arrayAdapter: ArrayAdapter<String>? = null

        // 어댑터에 값들을 spinner에 넣음
        petKind.adapter = adapter
        Log.d(TAG, "test" + petKind.adapter)

        var test = FBRef.codeInfoRef.child("code").get()

        Log.d(TAG, "test" + test)


        petKind.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }






    }
}

