package com.example.quick_med

import android.content.Context
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class Calendar : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var alarmTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // CalendarView 초기화
        calendarView = findViewById(R.id.Calendar)
        alarmTextView = findViewById(R.id.Alarm_TimeName)

        // 날짜 선택 리스너 설정
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$year/${month + 1}/$dayOfMonth"
            //Toast.makeText(this, "선택된 날짜: $selectedDate", Toast.LENGTH_SHORT).show()

            // 알람 정보 불러오기
            val sharedPreferences = getSharedPreferences("AlarmPreferences", Context.MODE_PRIVATE)
            val alarmName = sharedPreferences.getString("ALARM_NAME", null)
            val hour = sharedPreferences.getInt("ALARM_HOUR", -1)
            val minute = sharedPreferences.getInt("ALARM_MINUTE", -1)
            val repeatDays = BooleanArray(7)
            for (i in 0 until 7) {
                repeatDays[i] = sharedPreferences.getBoolean("ALARM_REPEAT_$i", false)
            }

            // 선택된 날짜의 요일 계산
            val calendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // 일요일 = 1, 월요일 = 2, ..., 토요일 = 7

            // 선택된 날짜의 요일에 알람이 있는지 확인하고 텍스트 초기화
            alarmTextView.text = ""
            if (alarmName != null && hour != -1 && minute != -1 && repeatDays[dayOfWeek - 1]) {
                val displayHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
                val displayMinute = String.format("%02d", minute)
                val amPm = if (hour >= 12) "PM" else "AM"
                val alarmInfo = "$displayHour:$displayMinute $amPm : $alarmName\n"
                alarmTextView.text = alarmInfo
            } else {
                alarmTextView.text = "선택된 날짜에 알람이 없습니다."
            }
        }
    }
}
