package com.example.quick_med

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SetAlarm : AppCompatActivity() {
    private companion object{
        const val REQUEST_CODE = 1
    }

    private lateinit var alarmListLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_set_alarm)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 오늘 날짜를 표시해줌.feat GPT
        setContentView(R.layout.activity_set_alarm)
        val dateTextView: TextView = findViewById(R.id.dateTextView)
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("M월 d일 E요일", Locale.KOREAN)
        val dateString = dateFormat.format(calendar.time)
        dateTextView.text = dateString

        alarmListLayout = findViewById(R.id.alarmListLayout)
        //알람 설정 화면으로 이동하는 버튼
        val button: Button = findViewById(R.id.addbutton)
        button.setOnClickListener {
            val intent = Intent(this, SetAlarm1::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                val label = data?.getStringExtra("label")
                val time = data?.getStringExtra("time")
                if (label != null && time != null){
                    addAlarmToList(label, time)
                }
            }
        }
        private fun addAlarmToList(label: String, time: String) {
            val alarmView = layoutInflater.inflate(R.layout.alarm_item, alarmListLayout,false)
            val alarmLabelTextView = alarmView.findViewById<TextView>(R.id.alarmLabelTextView)
            val alarmTimeTextView = alarmView.findViewById<TextView>(R.id.alarmTimeTextView)

            alarmLabelTextView.text = label
            alarmTimeTextView.text = time

            alarmListLayout.addView(alarmView,0)

        }
}