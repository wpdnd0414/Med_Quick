package com.example.quick_med

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SetAlarm : AppCompatActivity() {
    private companion object {
        const val REQUEST_CODE_ADD = 1 // 알람 추가 요청 코드
        const val REQUEST_CODE_MODIFY = 2 // 알람 수정 요청 코드
    }
    private lateinit var alarmListLayout: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var alarmList: MutableList<AlarmData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_set_alarm_0)

        // 시스템 바 패딩 설정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 오늘 날짜를 표시해줌
        val dateTextView: TextView = findViewById(R.id.dateTextView)
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("M월 d일 E요일", Locale.KOREAN)
        val dateString = dateFormat.format(calendar.time)
        dateTextView.text = dateString
        alarmListLayout = findViewById(R.id.alarmListLayout)
        sharedPreferences = getSharedPreferences("AlarmPreferences", Context.MODE_PRIVATE)

        // 알람 설정 화면으로 이동하는 버튼
        val button: Button = findViewById(R.id.addbutton)
        button.setOnClickListener {
            val intent = Intent(this, SetAlarm_Add::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD) // 알람 설정 화면으로 이동
        }
        val buttonback = findViewById<Button>(R.id.back_button)
        buttonback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // 저장된 알람 불러오기
        loadAlarms()
    }

    private fun loadAlarms() {
        alarmList = getAlarmList(sharedPreferences)
        for (i in alarmList.indices) {
            val alarmData = alarmList[i]
            val amPm = if (alarmData.hour >= 12) "PM" else "AM"
            val displayHour = if (alarmData.hour > 12) alarmData.hour - 12 else if (alarmData.hour == 0) 12 else alarmData.hour
            val displayMinute = String.format("%02d", alarmData.minute)
            val time = "$amPm $displayHour:$displayMinute"
            addAlarmToList(alarmData, time, i)
        }
    }

    private fun getAlarmList(sharedPreferences: SharedPreferences): MutableList<AlarmData> {
        val json = sharedPreferences.getString("ALARM_LIST", null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<AlarmData>>() {}.type
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val label = data?.getStringExtra("ALARM_NAME")
            val hour = data?.getIntExtra("ALARM_HOUR", -1) ?: -1
            val minute = data?.getIntExtra("ALARM_MINUTE", -1) ?: -1
            val alarmIndex = data?.getIntExtra("ALARM_INDEX", -1) ?: -1
            val isEnabled = data?.getBooleanExtra("ALARM_ENABLED", true) ?: true

            if (label != null && hour != -1 && minute != -1) {
                val time = String.format("%02d:%02d", hour, minute)
                val alarmData = AlarmData(label, hour, minute, BooleanArray(7), isEnabled)

                if (requestCode == REQUEST_CODE_ADD) {
                    addAlarmToList(alarmData, time, alarmList.size)
                    saveAlarm(alarmData)
                } else if (requestCode == REQUEST_CODE_MODIFY && alarmIndex != -1) {
                    modifyAlarmInList(alarmIndex, alarmData, time)
                    updateAlarm(alarmIndex, alarmData)
                }
            } else if (requestCode == REQUEST_CODE_MODIFY && alarmIndex != -1) {
                alarmListLayout.removeViewAt(alarmIndex)
                removeAlarm(alarmIndex)
            }
        }
    }

    private fun saveAlarm(alarmData: AlarmData) {
        alarmList.add(alarmData)
        val editor = sharedPreferences.edit()
        editor.putString("ALARM_LIST", Gson().toJson(alarmList))
        editor.apply()
    }

    private fun updateAlarm(index: Int, alarmData: AlarmData) {
        alarmList[index] = alarmData
        val editor = sharedPreferences.edit()
        editor.putString("ALARM_LIST", Gson().toJson(alarmList))
        editor.apply()
    }

    private fun removeAlarm(index: Int) {
        alarmList.removeAt(index)
        val editor = sharedPreferences.edit()
        editor.putString("ALARM_LIST", Gson().toJson(alarmList))
        editor.apply()
    }

    @SuppressLint("MissingInflatedId")
    private fun addAlarmToList(alarmData: AlarmData, time: String, index: Int) {
        val alarmView = layoutInflater.inflate(R.layout.alarm_item, alarmListLayout, false)
        val alarmLabelTextView = alarmView.findViewById<TextView>(R.id.alarmLabelTextView)
        val alarmTimeTextView = alarmView.findViewById<TextView>(R.id.alarmTimeTextView)
        val alarmSwitch = alarmView.findViewById<Switch>(R.id.alarmSwitch)

        alarmLabelTextView.text = alarmData.name
        alarmTimeTextView.text = time
        alarmSwitch.isChecked = alarmData.isEnabled

        alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
            alarmData.isEnabled = isChecked
            updateAlarm(index, alarmData)
        }

        alarmView.setOnClickListener {
            val intent = Intent(this, SetAlarm_Modify::class.java).apply {
                putExtra("ALARM_NAME", alarmData.name)
                putExtra("ALARM_HOUR", alarmData.hour)
                putExtra("ALARM_MINUTE", alarmData.minute)
                putExtra("ALARM_INDEX", index)
                putExtra("ALARM_ENABLED", alarmData.isEnabled)
            }
            startActivityForResult(intent, REQUEST_CODE_MODIFY)
        }

        alarmListLayout.addView(alarmView, index)
    }

    private fun modifyAlarmInList(index: Int, alarmData: AlarmData, time: String) {
        val alarmView = layoutInflater.inflate(R.layout.alarm_item, alarmListLayout, false)
        val alarmLabelTextView = alarmView.findViewById<TextView>(R.id.alarmLabelTextView)
        val alarmTimeTextView = alarmView.findViewById<TextView>(R.id.alarmTimeTextView)
        val alarmSwitch = alarmView.findViewById<Switch>(R.id.alarmSwitch)

        alarmLabelTextView.text = alarmData.name
        alarmTimeTextView.text = time
        alarmSwitch.isChecked = alarmData.isEnabled

        alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
            alarmData.isEnabled = isChecked
            updateAlarm(index, alarmData)
        }

        alarmListLayout.removeViewAt(index)
        alarmListLayout.addView(alarmView, index)

        alarmView.setOnClickListener {
            val intent = Intent(this, SetAlarm_Modify::class.java).apply {
                putExtra("ALARM_NAME", alarmData.name)
                putExtra("ALARM_HOUR", alarmData.hour)
                putExtra("ALARM_MINUTE", alarmData.minute)
                putExtra("ALARM_INDEX", index)
                putExtra("ALARM_ENABLED", alarmData.isEnabled)
            }
            startActivityForResult(intent, REQUEST_CODE_MODIFY)
        }
    }
}