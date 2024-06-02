package com.example.quick_med

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class SetAlarm_Add : AppCompatActivity() {

    private lateinit var spinnerAmPm: Spinner
    private lateinit var numberPickerHour: NumberPicker
    private lateinit var numberPickerMinute: NumberPicker
    private lateinit var editTextAlarmName: EditText
    private lateinit var dayCheckBoxes: Array<CheckBox>
    private lateinit var textViewCurrentAlarm: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_alarm_2)                     //알람 수정 (alarm_2)
        requestNotificationPermission(this)

        // 뷰 초기화
        spinnerAmPm = findViewById(R.id.spinner_am_pm)
        numberPickerHour = findViewById(R.id.numberPicker_hour)
        numberPickerMinute = findViewById(R.id.numberPicker_minute)
        editTextAlarmName = findViewById(R.id.editText_alarm_name)
        textViewCurrentAlarm = findViewById(R.id.textView_current_alarm)
        dayCheckBoxes = arrayOf(
            findViewById(R.id.checkBox_sunday),
            findViewById(R.id.checkBox_monday),
            findViewById(R.id.checkBox_tuesday),
            findViewById(R.id.checkBox_wednesday),
            findViewById(R.id.checkBox_thursday),
            findViewById(R.id.checkBox_friday),
            findViewById(R.id.checkBox_saturday)
        )

        // NumberPicker 범위 설정
        numberPickerHour.minValue = 1
        numberPickerHour.maxValue = 12
        numberPickerMinute.minValue = 0
        numberPickerMinute.maxValue = 59

        // 버튼 클릭 리스너 설정
        val buttonCreateAlarm: Button = findViewById(R.id.button_create_alarm)
        val buttonCancelAlarm: Button = findViewById(R.id.button_cancel_alarm)
        val buttonDeleteAlarm: Button = findViewById(R.id.button_delete_alarm)
        buttonCreateAlarm.setOnClickListener { setAlarm() }
        buttonCancelAlarm.setOnClickListener { finish() }
        buttonDeleteAlarm.setOnClickListener { deleteAlarm() }

        displayCurrentAlarm()
    }

    private fun setAlarm() {
        val amPm = spinnerAmPm.selectedItemPosition // 0은 AM, 1은 PM
        var hour = numberPickerHour.value
        val minute = numberPickerMinute.value
        val alarmName = editTextAlarmName.text.toString()

        // 24시간 형식으로 변환
        if (amPm == 1 && hour != 12) {
            hour += 12
        } else if (amPm == 0 && hour == 12) {
            hour = 0
        }

        // 선택된 시간으로 캘린더 설정
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1) // 현재 시간보다 이전이면 다음 날로 설정
            }
        }

        // 선택된 요일 확인
        val repeatDays = BooleanArray(7)
        for (i in dayCheckBoxes.indices) {
            repeatDays[i] = dayCheckBoxes[i].isChecked
        }

        // 알람 데이터를 SharedPreferences에 저장
        val sharedPreferences = getSharedPreferences("AlarmPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val alarmList = getAlarmList(sharedPreferences)
        alarmList.add(AlarmData(alarmName, hour, minute, repeatDays, true)) // 활성화 상태는 true로 설정
        editor.putString("ALARM_LIST", Gson().toJson(alarmList))
        editor.apply()

        // 알람 설정
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("ALARM_NAME", alarmName)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (repeatDays.contains(true)) {
            for (i in repeatDays.indices) {
                if (repeatDays[i]) {
                    calendar.set(Calendar.DAY_OF_WEEK, i + 1)
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY * 7, pendingIntent
                    )
                }
            }
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
        Toast.makeText(this, "알람이 설정되었습니다", Toast.LENGTH_SHORT).show()

        // 설정한 알람 데이터를 인텐트에 추가하여 반환
        val resultIntent = Intent().apply {
            putExtra("ALARM_NAME", alarmName)
            putExtra("ALARM_HOUR", hour)
            putExtra("ALARM_MINUTE", minute)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
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

    private fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(
                    Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            }
        }
    }

    private fun deleteAlarm() {
        // 알람 삭제
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)

        // SharedPreferences에서 알람 데이터 삭제
        val sharedPreferences = getSharedPreferences("AlarmPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        Toast.makeText(this, "알람이 삭제되었습니다", Toast.LENGTH_SHORT).show()
        displayCurrentAlarm()
    }

    private fun displayCurrentAlarm() {
        // SharedPreferences에서 알람 데이터를 읽어옵니다.
        val sharedPreferences = getSharedPreferences("AlarmPreferences", Context.MODE_PRIVATE)
        val alarmList = getAlarmList(sharedPreferences)

        if (alarmList.isNotEmpty()) {
            val alarmData = alarmList[0]
            val amPm = if (alarmData.hour >= 12) "PM" else "AM"
            val displayHour =
                if (alarmData.hour > 12) alarmData.hour - 12 else if (alarmData.hour == 0) 12 else alarmData.hour
            val displayMinute = String.format("%02d", alarmData.minute)
            textViewCurrentAlarm.text =
                "현재 설정된 알람: ${alarmData.name} - $amPm $displayHour:$displayMinute"
        } else {
            textViewCurrentAlarm.text = "현재 설정된 알람 없음"
        }
    }

    fun requestNotificationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(activity, "android.permission.POST_NOTIFICATIONS") != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf("android.permission.POST_NOTIFICATIONS"), 1)
            }
        }
    }
}