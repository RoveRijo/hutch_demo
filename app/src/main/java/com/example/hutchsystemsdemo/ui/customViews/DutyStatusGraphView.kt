package com.example.hutchsystemsdemo.ui.customViews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.hutchsystemsdemo.dataClasses.DutyStatus
import com.example.hutchsystemsdemo.dataClasses.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DutyStatusGraphView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var dutyStatusList: List<DutyStatus> = listOf(
        DutyStatus(DutyStatus.OFF_DUTY, 5, 6),
        DutyStatus(DutyStatus.OFF_DUTY, 7, 8),
        DutyStatus(DutyStatus.OFF_DUTY, 7, 8),
        DutyStatus(DutyStatus.SLEEPER, 11, 12),
        DutyStatus(DutyStatus.DRIVING, 4, 5),
        DutyStatus(DutyStatus.ON_DUTY, 3, 4)
    )
    private var selectedStatusIndex = -1

    init {
        // Load duty status data from SharedPreferences
        //dutyStatusList = loadDutyStatusData(context.applicationContext)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val widthPerHour = 50f // 1 hour is 50 pixels
        val graphHeight = height.toFloat() // Use the full height of the view for the graph
        val lineHeight =
            graphHeight / 5 // Divide the height by the number of statuses (4) + 1 for the x-axis
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 30f // Set the desired text size
        }
        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 5f // Set the desired line thickness
        }

        // Draw the x-axis and y-axis
        canvas.drawLine(0f, lineHeight * 4, width.toFloat(), lineHeight * 4, linePaint) // x-axis
        canvas.drawLine(0f, 0f, 0f, graphHeight, linePaint) // y-axis

        // Draw the lines for each duty status
        dutyStatusList.forEach { dutyStatus ->
            val startX = dutyStatus.startTime * widthPerHour
            val endX = dutyStatus.endTime * widthPerHour
            val yPosition = when (dutyStatus.status) {
                DutyStatus.OFF_DUTY -> lineHeight * 0
                DutyStatus.SLEEPER -> lineHeight * 1
                DutyStatus.DRIVING -> lineHeight * 2
                DutyStatus.ON_DUTY -> lineHeight * 3
                else -> 0f
            }

            // Change the line color to blue when the status is DRIVING
            linePaint.color = if (dutyStatus.status == DutyStatus.DRIVING) Color.BLUE else Color.RED

            // Draw the status line
            canvas.drawLine(startX, yPosition, endX, yPosition, linePaint)
        }

        // Draw the total time spent in each status on the right side
        val totalTimePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 30f // Set the desired text size
            textAlign = Paint.Align.RIGHT // Align text to the right
        }
        val totalHoursYOffset = lineHeight / 2 // Center the text vertically in each line
        val totalHoursXPosition = width.toFloat() - 10 // 10 pixels padding from the right edge

        // Assuming you have a method to calculate total hours for each status
        val totalOffDutyHours = calculateTotalHours(dutyStatusList, DutyStatus.OFF_DUTY)
        val totalSleeperHours = calculateTotalHours(dutyStatusList, DutyStatus.SLEEPER)
        val totalDrivingHours = calculateTotalHours(dutyStatusList, DutyStatus.DRIVING)
        val totalOnDutyHours = calculateTotalHours(dutyStatusList, DutyStatus.ON_DUTY)

        canvas.drawText(
            "$totalOffDutyHours",
            totalHoursXPosition,
            lineHeight * 0 + totalHoursYOffset,
            totalTimePaint
        )
        canvas.drawText(
            "$totalSleeperHours",
            totalHoursXPosition,
            lineHeight * 1 + totalHoursYOffset,
            totalTimePaint
        )
        canvas.drawText(
            "$totalDrivingHours",
            totalHoursXPosition,
            lineHeight * 2 + totalHoursYOffset,
            totalTimePaint
        )
        canvas.drawText(
            "$totalOnDutyHours",
            totalHoursXPosition,
            lineHeight * 3 + totalHoursYOffset,
            totalTimePaint
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.x.toInt()
            val y = event.y.toInt()
            // Determine which duty status block was clicked
            // Update the selectedStatusIndex and dutyStatusList accordingly
            // Invalidate the view to trigger a redraw
        }
        return true
    }

    private fun saveDutyStatusData(context: Context, dutyStatusList: List<DutyStatus>) {
        val sharedPreferences =
            context.getSharedPreferences("DutyStatusPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(dutyStatusList)
        editor.putString("DutyStatusData", json)
        editor.apply()
    }

    private fun calculateTotalHours(dutyStatusList: List<DutyStatus>, status: String): Int {
        return dutyStatusList
            .filter { it.status == status }
            .sumOf { it.endTime - it.startTime }
    }

    private fun loadDutyStatusData(context: Context): List<DutyStatus> {
        val sharedPreferences =
            context.getSharedPreferences("DutyStatusPrefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("DutyStatusData", null)
        val type = object : TypeToken<List<DutyStatus>>() {}.type
        return Gson().fromJson(json, type) ?: emptyList()
    }

    // Helper methods to draw the graph and handle touch events
}