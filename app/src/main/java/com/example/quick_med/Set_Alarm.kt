package com.example.quick_med

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Set_Alarm : AppCompatActivity() {
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

        val dateFormat = SimpleDateFormat("M월 d일 E요일", Locale.KOREAN )

        val dateString = dateFormat.format(calendar.time)

        dateTextView.text = dateString



        /*val button: Button = findViewById(R.id.addbutton)

        button.setOnClickListener {
            val intent = Intent(this, /*이 안에 Set_Alarm 1이름*/ ::class.java)
            startActivity(intent)*/
    }
}