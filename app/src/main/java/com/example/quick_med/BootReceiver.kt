package com.example.quick_med

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.AlarmManager
import android.app.PendingIntent
import java.util.Calendar

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // 부팅이 완료되었을 때 알람을 다시 설정합니다.
            setAlarmsAfterReboot(context)
        }
    }

    private fun setAlarmsAfterReboot(context: Context) {
        // SharedPreferences에서 알람 데이터를 읽어옵니다.
        val sharedPreferences = context.getSharedPreferences("AlarmPreferences", Context.MODE_PRIVATE)
        val alarmName = sharedPreferences.getString("ALARM_NAME", "")
        val hour = sharedPreferences.getInt("ALARM_HOUR", 0)
        val minute = sharedPreferences.getInt("ALARM_MINUTE", 0)
        val repeatDays = BooleanArray(7)
        for (i in 0 until 7) {
            repeatDays[i] = sharedPreferences.getBoolean("ALARM_REPEAT_$i", false)
        }

        // 알람 재설정
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("ALARM_NAME", alarmName)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (repeatDays[calendar.get(Calendar.DAY_OF_WEEK) - 1]) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY * 7, pendingIntent
            )
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }
}