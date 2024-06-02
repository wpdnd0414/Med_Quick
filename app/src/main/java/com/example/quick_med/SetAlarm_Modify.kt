package com.example.quick_med

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity

class SetAlarm_Modify : AppCompatActivity() {
    private lateinit var alarmLabelEditText: EditText
    private lateinit var alarmTimePicker: TimePicker
    private lateinit var deleteButton: Button
    private lateinit var saveButton: Button
    private var alarmIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_alarm_1)

        alarmLabelEditText = findViewById(R.id.alarmLabelEditText)
        alarmTimePicker = findViewById(R.id.alarmTimePicker)
        deleteButton = findViewById(R.id.deleteButton)
        saveButton = findViewById(R.id.saveButton)

        // Intent로부터 알람 데이터를 가져옴
        val alarmName = intent.getStringExtra("ALARM_NAME")
        val alarmHour = intent.getIntExtra("ALARM_HOUR", -1)
        val alarmMinute = intent.getIntExtra("ALARM_MINUTE", -1)
        alarmIndex = intent.getIntExtra("ALARM_INDEX", -1)

        if (alarmName != null && alarmHour != -1 && alarmMinute != -1) {
            alarmLabelEditText.setText(alarmName)
            alarmTimePicker.hour = alarmHour
            alarmTimePicker.minute = alarmMinute
        }

        deleteButton.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("ALARM_INDEX", alarmIndex)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        saveButton.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("ALARM_NAME", alarmLabelEditText.text.toString())
            resultIntent.putExtra("ALARM_HOUR", alarmTimePicker.hour)
            resultIntent.putExtra("ALARM_MINUTE", alarmTimePicker.minute)
            resultIntent.putExtra("ALARM_INDEX", alarmIndex)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}