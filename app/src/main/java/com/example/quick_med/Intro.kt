package com.example.quick_med

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.quick_med.ui.theme.Quick_MedTheme
import android.os.Handler
import android.content.Intent
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Intro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        // Ensure the ImageView is being set properly
        val introImageView = findViewById<ImageView>(R.id.introImageView)

        // 3초 후에  액티비티로 전환
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Intro2::class.java)
            startActivity(intent)
            finish() // 인트로 액티비티를 종료하여 뒤로 가기 버튼을 눌렀을 때 다시 돌아오지 않게 함
        }, 3000) // 3000밀리초 == 3초
    }
}