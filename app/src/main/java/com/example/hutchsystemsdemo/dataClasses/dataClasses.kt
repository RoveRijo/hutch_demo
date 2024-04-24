package com.example.hutchsystemsdemo.dataClasses

data class DutyStatus(
    val status: String,
    val startTime: Int, // in hours
    val endTime: Int // in hours
) {

    companion object {
        const val OFF_DUTY = "Off-Duty"
        const val SLEEPER = "Sleeper Berth"
        const val DRIVING = "Driving"
        const val ON_DUTY = "On-Duty"

    }
}

