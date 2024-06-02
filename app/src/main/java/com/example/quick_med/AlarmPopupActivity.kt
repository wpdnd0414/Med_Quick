package com.example.quick_med

import android.app.Activity
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class AlarmPopupActivity : Activity() {

    private lateinit var ringtone: Ringtone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_popup)

        val alarmName = intent.getStringExtra("ALARM_NAME")
        val alarmNameTextView: TextView = findViewById(R.id.alarmNameTextView)
        alarmNameTextView.text = alarmName

        val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(this, alarmUri)
        ringtone.play()

        val stopAlarmButton: Button = findViewById(R.id.stopAlarmButton)
        stopAlarmButton.setOnClickListener {
            ringtone.stop()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (ringtone.isPlaying) {
            ringtone.stop()
        }
    }
}