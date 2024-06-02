package com.example.quick_med

data class AlarmData(
    val name: String,
    val hour: Int,
    val minute: Int,
    val daysOfWeek: BooleanArray,
    var isEnabled: Boolean // 알람 활성화 상태를 저장하는 변수
)