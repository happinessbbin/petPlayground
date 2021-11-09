package com.team.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.FragmentContainerView
import com.team.project.setting.SettingActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.team.project.databinding.ActivityMyInfoBinding
import com.team.project.fragments.DatePickerFragment.Companion.newInstance
import com.team.project.fragments.HomeFragment
import com.team.project.fragments.MyInfoFragment.Companion.newInstance

class MainActivity : AppCompatActivity() {

    lateinit var uid : String
    lateinit var email : String

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ImageView>(R.id.settingBtn).setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}