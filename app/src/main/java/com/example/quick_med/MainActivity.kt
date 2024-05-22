package com.example.quick_med

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonAlarm = findViewById<Button>(R.id.button_alarm)
        buttonAlarm.setOnClickListener {
            val intent = Intent(this, Set_Alarm::class.java)
            startActivity(intent)
        }

        val buttonCalendar = findViewById<Button>(R.id.button_calendar)
        buttonCalendar.setOnClickListener {
            val intent = Intent(this, Calendar::class.java)
            startActivity(intent)
        }

        val buttonMyMed = findViewById<Button>(R.id.button_my_med)
        buttonMyMed.setOnClickListener {
            val intent = Intent(this, My_Med::class.java)
            startActivity(intent)
        }

        val buttonSearchMed = findViewById<Button>(R.id.button_search_med)
        buttonSearchMed.setOnClickListener {
            val intent = Intent(this, SearchMed::class.java)
            startActivity(intent)
        }
    }
}

