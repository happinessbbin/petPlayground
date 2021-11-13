package com.team.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Build
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.team.project.R
import java.util.*


class Nfc : AppCompatActivity() {
    private var TAG: String = "MainActivity"
    private lateinit var nfcPendingIntent: PendingIntent
    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)

        // 핸드폰 번호 받아오기
        phoneNumber = intent.getStringExtra("phoneNumber").toString()
        
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcPendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )


    }

    override fun onResume() {
        super.onResume()
        nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, null, null);
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val detectedTag : Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)

        val writeValue : String = "tel:" + phoneNumber


        val message: NdefMessage = createTagMessage(writeValue)

        writeTag(message, detectedTag!!)
    }

    private fun createTagMessage(msg: String): NdefMessage {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            NdefMessage(NdefRecord.createUri(msg))
        } else {
            TODO("VERSION.SDK_INT < JELLY_BEAN")
        }
    }

    fun writeTag(message: NdefMessage, tag: Tag) {
        val size = message.toByteArray().size
        try {
            val ndef = Ndef.get(tag)
            if (ndef != null) {
                ndef.connect()
                if (!ndef.isWritable) {
                    Toast.makeText(applicationContext, "NFC태그에 등록되지 않았습니다.", Toast.LENGTH_SHORT).show()
                }
                ndef.writeNdefMessage(message)
                Toast.makeText(applicationContext, "NFC태그에 전화번호를 등록 하였습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        } catch (e: Exception) {

        }
    }
}