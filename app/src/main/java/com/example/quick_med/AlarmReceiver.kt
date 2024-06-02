package com.example.quick_med

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private var ringtone: Ringtone? = null
        const val STOP_ALARM_ACTION = "com.example.quick_med.STOP_ALARM"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val alarmName = intent.getStringExtra("ALARM_NAME")

        if (intent.action == STOP_ALARM_ACTION) {
            stopAlarm()
        } else {
            showNotification(context, alarmName)
            playAlarm(context)
        }
    }

    private fun playAlarm(context: Context) {
        val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(context, alarmUri)
        ringtone?.play()
    }

    private fun stopAlarm() {
        ringtone?.stop()
    }

    private fun showNotification(context: Context, alarmName: String?) {
        val channelId = "alarm_channel"
        val channelName = "Alarm Notification"

        // NotificationChannel 생성 (오레오 이상 버전 필요)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Channel for alarm notifications"
            }

            // NotificationManager를 통해 채널 등록
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // 알람 끄기 인텐트 설정
        val stopAlarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = STOP_ALARM_ACTION
        }
        val stopAlarmPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context, 0, stopAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 알림 클릭 시 알람 팝업을 띄우는 인텐트 설정
        val popupIntent = Intent(context, AlarmPopupActivity::class.java).apply {
            putExtra("ALARM_NAME", alarmName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val popupPendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, popupIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 알림 생성
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.logo2) // 알림 아이콘 설정
            .setContentTitle("알람")
            .setContentText(alarmName ?: "알람이 울립니다!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(popupPendingIntent)
            .addAction(R.drawable.logo, "알람 끄기", stopAlarmPendingIntent)
            .setAutoCancel(true)

        // 알림 표시
        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }
}