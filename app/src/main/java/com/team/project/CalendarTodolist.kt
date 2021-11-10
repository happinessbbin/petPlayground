package com.team.project


import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

//이 페이지는 fragment_tip 페이지에서 글작성버튼 누르면 액티비티 새로 생성하는 페이지
class CalendarTodolist : AppCompatActivity() {
    lateinit var myRef : DatabaseReference
    lateinit var dp : DatePicker
    lateinit var edtDiary : EditText
    lateinit var btnWrite : Button
    lateinit var fileName : String

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_todolist)
        val database = Firebase.database
        val todolist = intent.getStringExtra("totolist")
        myRef = database.getReference("todolist")
        title = "간단 일기장 (수정)"

        dp = findViewById<DatePicker>(R.id.datePicker1)
        edtDiary = findViewById<EditText>(R.id.edtDiary)
        btnWrite = findViewById<Button>(R.id.btnWrite)

        var cal = Calendar.getInstance()
        var cYear = cal.get(Calendar.YEAR)
        var cMonth = cal.get(Calendar.MONTH)
        var cDay = cal.get(Calendar.DAY_OF_MONTH)

        // 처음 실행시에 설정할 내용
        fileName = (Integer.toString(cYear) + "_" + Integer.toString(cMonth + 1)
                + "_" + Integer.toString(cDay) + ".txt")
        var str = readDiary(fileName)
        edtDiary.setText(str)

        dp.init(cYear, cMonth, cDay) { view, year, monthOfYear, dayOfMonth ->
            fileName = (Integer.toString(year) + "_"
                    + Integer.toString(monthOfYear + 1) + "_"
                    + Integer.toString(dayOfMonth) + ".txt")
            var str = readDiary(fileName)
            edtDiary.setText(str)
        }

        btnWrite.setOnClickListener {
            var outFs = openFileOutput(fileName, Context.MODE_PRIVATE)
            var str = edtDiary.text.toString()
            outFs.write(str.toByteArray())
            outFs.close()
            Toast.makeText(applicationContext, "$fileName 이 저장됨", Toast.LENGTH_SHORT).show()
        }

    }

    fun readDiary(fName: String) : String? {
        var diaryStr : String? = null
        var inFs: FileInputStream
        try {
            inFs = openFileInput(fName)
            var txt = ByteArray(500)
            inFs.read(txt)
            inFs.close()
            diaryStr = txt.toString(Charsets.UTF_8).trim()
            btnWrite.text = "수정 하기"
        } catch (e : IOException) {
            edtDiary.hint = "일기 없음"
            btnWrite.text = "새로 저장"
        }

        return diaryStr
    }

}