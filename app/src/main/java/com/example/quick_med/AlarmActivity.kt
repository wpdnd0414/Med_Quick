package com.example.quick_med

import android.os.Bundle
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity

class AlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_alarm_2)

        val hourPicker: NumberPicker = findViewById(R.id.numberPicker_hour)
        val minutePicker: NumberPicker = findViewById(R.id.numberPicker_minute)

        // 시간 선택 설정
        hourPicker.minValue = 1
        hourPicker.maxValue = 12

        // 분 선택 설정
        minutePicker.minValue = 0
        minutePicker.maxValue = 59
        minutePicker.setFormatter { value -> String.format("%02d", value) }
    }
}
