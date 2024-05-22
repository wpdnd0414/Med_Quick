package com.example.quick_med

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity

class SetAlarm1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_alarm1)

        val alarmLabelEditText: EditText = findViewById(R.id.alarmLabelEditText)
        val timePicker: TimePicker = findViewById(R.id.timePicker)
        val setAlarmButton: Button = findViewById(R.id.setAlarmButton)

        setAlarmButton.setOnClickListener {
            val label = alarmLabelEditText.text.toString()
            val hour = timePicker.hour
            val minute = timePicker.minute
            val time = String.format("%02d:%02d", hour, minute)

            val resultIntent = Intent()
            resultIntent.putExtra("label", label)
            resultIntent.putExtra("time", time)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}