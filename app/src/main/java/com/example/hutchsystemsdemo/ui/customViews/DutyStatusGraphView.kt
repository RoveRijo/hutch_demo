package com.example.hutchsystemsdemo.ui.customViews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.hutchsystemsdemo.dataClasses.DutyStatus
import com.example.hutchsystemsdemo.dataClasses.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DutyStatusGraphView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    constructor(
        context: Context,
        attrs: AttributeSet?,
        dutyStatusList: MutableList<DutyStatus>
    ) : this(
        context,
        attrs
    ) {
        this.dutyStatusList = dutyStatusList
    }

    private var dutyStatusList: MutableList<DutyStatus> = mutableListOf()

    // Lambda property for click listener
    var onClick: ((dutyStatus: DutyStatus) -> Unit)? = null

    private var offSetStart = 200f
    private var offSetEnd = 200f
    private var offSetTop = 100f
    private var offSetBottom = 100f
    private var widthPerHour = (width.toFloat() - (offSetStart + offSetEnd)) / 24
    private var graphHeight = height.toFloat()
    private var lineHeight =
        (graphHeight - (offSetTop + offSetBottom)) / 4

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        offSetStart = 200f
        offSetEnd = 200f
        offSetTop = 100f
        offSetBottom = 100f
        widthPerHour = (width.toFloat() - (offSetStart + offSetEnd)) / 24
        graphHeight = height.toFloat() // Use the full height of the view for the graph
        lineHeight =
            (graphHeight - (offSetTop + offSetBottom)) / 4
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 30f // Set the desired text size
        }
        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 3f // Set the desired line thickness
            color = Color.BLACK
        }

        // Draw the x-axis and y-axis
        canvas.drawLine(
            0f + offSetStart,
            graphHeight - offSetBottom,
            width.toFloat() - offSetEnd,
            graphHeight - offSetBottom,
            linePaint
        ) // x-axis
        canvas.drawLine(
            0f + offSetStart,
            0f + offSetTop,
            0f + offSetStart,
            graphHeight - offSetBottom,
            linePaint
        ) // y-axis

        canvas.drawLine(
            width.toFloat() - offSetEnd,
            graphHeight - offSetBottom,
            width.toFloat() - offSetEnd,
            0f + offSetTop,
            linePaint
        )
        canvas.drawLine(
            width.toFloat() - offSetEnd,
            0f + offSetTop,
            0f + offSetStart,
            0f + offSetTop,
            linePaint
        )

        //Draw the 24 equal divisions and vertical lines
        for (i in 1..23) {
            val x = i * widthPerHour + offSetStart
            // Draw the vertical line
            canvas.drawLine(x, 0f + offSetTop, x, graphHeight - offSetBottom, linePaint)

            // Draw the hour marker
            canvas.drawText("$i", x - 10f, graphHeight - offSetBottom + 30f, textPaint)
            canvas.drawText("$i", x - 10f, 0f + offSetTop - 10f, textPaint)
        }
        // mark 1st and last hour markers
        canvas.drawText("0", offSetStart - 10f, graphHeight - offSetBottom + 30f, textPaint)
        canvas.drawText("0", offSetStart - 10f, 0f + offSetTop - 10f, textPaint)
        canvas.drawText("24", width - offSetEnd - 10f, graphHeight - offSetBottom + 30f, textPaint)
        canvas.drawText("24", width - offSetEnd - 10f, 0f + offSetTop - 10f, textPaint)

        //Draw the 4 equal divisions and horizontal lines
        for (i in 1..3) {
            val y = i * lineHeight + offSetTop
            // Draw the horizontal line
            canvas.drawLine(0f, y, width.toFloat(), y, linePaint)

            // Draw the duty status text
            canvas.drawText(
                when (i) {
                    1 -> "Off-Duty"
                    2 -> "Sleeper Berth"
                    3 -> "Driving"
                    else -> ""
                }, 0f, y - 50f, textPaint
            )

        }
        // draw last duty status at bottom
        canvas.drawText("On-Duty", 0f, graphHeight - offSetBottom - 50f, textPaint)

        // draw remaining lines
        canvas.drawLine(
            0f,
            graphHeight - offSetBottom,
            0f + offSetStart,
            graphHeight - offSetBottom,
            linePaint
        )
        canvas.drawLine(
            width.toFloat() - offSetEnd,
            graphHeight - offSetBottom,
            width.toFloat(),
            graphHeight - offSetBottom,
            linePaint
        )
        // Draw remaining texts
        textPaint.apply {
            typeface = Typeface.DEFAULT_BOLD
        }
        canvas.drawText("DUTY STATUS", 0f, 50f, textPaint)
        canvas.drawText("Total Hours", width.toFloat() - offSetEnd + 20f, 50f, textPaint)

        // Draw the lines for each duty status
        dutyStatusList.forEachIndexed { index, dutyStatus ->
            val startX = dutyStatus.startTime * widthPerHour + offSetStart
            val endX = dutyStatus.endTime * widthPerHour + offSetStart
            val yPosition = when (dutyStatus.status) {
                DutyStatus.OFF_DUTY -> lineHeight * 0 + offSetTop + (lineHeight / 2)
                DutyStatus.SLEEPER -> lineHeight * 1 + offSetTop + (lineHeight / 2)
                DutyStatus.DRIVING -> lineHeight * 2 + offSetTop + (lineHeight / 2)
                DutyStatus.ON_DUTY -> lineHeight * 3 + offSetTop + (lineHeight / 2)
                else -> 0f
            }
            linePaint.strokeWidth = 10f
            // Change the line color to blue when the status is DRIVING
            linePaint.color = if (dutyStatus.status == DutyStatus.DRIVING) Color.BLUE else Color.RED

            // Draw the status line
            canvas.drawLine(startX, yPosition, endX, yPosition, linePaint)
            // Reset line color back to red
            linePaint.color = Color.RED

            // Connect this line to the next one if it's not the last item
            if (index < dutyStatusList.size - 1) {
                val nextDutyStatus = dutyStatusList[index + 1]
                val nextStartX = nextDutyStatus.startTime * widthPerHour + offSetStart
                val nextYPosition = when (nextDutyStatus.status) {
                    DutyStatus.OFF_DUTY -> lineHeight * 0 + offSetTop + (lineHeight / 2)
                    DutyStatus.SLEEPER -> lineHeight * 1 + offSetTop + (lineHeight / 2)
                    DutyStatus.DRIVING -> lineHeight * 2 + offSetTop + (lineHeight / 2)
                    DutyStatus.ON_DUTY -> lineHeight * 3 + offSetTop + (lineHeight / 2)
                    else -> 0f
                }

                //Draw a line connecting the end of the current status to the start of the next
                canvas.drawLine(endX, yPosition, nextStartX, nextYPosition, linePaint)
            }
        }

        // Draw the total time spent in each status on the right side
        val totalTimePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 30f // Set the desired text size
            textAlign = Paint.Align.RIGHT // Align text to the right
        }

        val totalOffDutyHours = calculateTotalHours(dutyStatusList, DutyStatus.OFF_DUTY)
        val totalSleeperHours = calculateTotalHours(dutyStatusList, DutyStatus.SLEEPER)
        val totalDrivingHours = calculateTotalHours(dutyStatusList, DutyStatus.DRIVING)
        val totalOnDutyHours = calculateTotalHours(dutyStatusList, DutyStatus.ON_DUTY)
        val totalHours =
            totalOffDutyHours + totalSleeperHours + totalDrivingHours + totalOnDutyHours

        canvas.drawText(
            "$totalOffDutyHours",
            width.toFloat() - offSetEnd + 100f,
            0 * lineHeight + offSetTop + lineHeight / 2,
            totalTimePaint
        )
        canvas.drawText(
            "$totalSleeperHours",
            width.toFloat() - offSetEnd + 100f,
            1 * lineHeight + offSetTop + lineHeight / 2,
            totalTimePaint
        )
        canvas.drawText(
            "$totalDrivingHours",
            width.toFloat() - offSetEnd + 100f,
            2 * lineHeight + offSetTop + lineHeight / 2,
            totalTimePaint
        )
        canvas.drawText(
            "$totalOnDutyHours",
            width.toFloat() - offSetEnd + 100f,
            3 * lineHeight + offSetTop + lineHeight / 2,
            totalTimePaint
        )
        canvas.drawText(
            "$totalHours",
            width.toFloat() - offSetEnd + 100f,
            graphHeight - offSetBottom + 50f,
            totalTimePaint
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }

            MotionEvent.ACTION_UP -> {
                val dutyStatus = getDutyStatusFromClick(event.x, event.y)
                if (dutyStatus != null) {
                    onClick?.invoke(dutyStatus)
                    dutyStatusList.add(dutyStatus)
                    invalidate()
                }
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event) // For other actions, defer to the superclass implementation
    }

    private fun calculateTotalHours(dutyStatusList: List<DutyStatus>, status: String): Int {
        return dutyStatusList
            .filter { it.status == status }
            .sumOf { it.endTime - it.startTime }
    }

    // Helper methods to draw the graph and handle touch events
    private fun getDutyStatusFromClick(x: Float, y: Float): DutyStatus? {
        // check if x and y is inside the graph
        if (x > offSetStart && x < width - offSetEnd && y > offSetTop && y < graphHeight - offSetBottom) {
            val col: Int = ((x - offSetStart) / widthPerHour).toInt()
            val row: Int = ((y - offSetTop) / lineHeight).toInt()
            val dutyStatus = when (row) {
                0 -> DutyStatus.OFF_DUTY
                1 -> DutyStatus.SLEEPER
                2 -> DutyStatus.DRIVING
                3 -> DutyStatus.ON_DUTY
                else -> null
            }
            if (dutyStatus != null && col == dutyStatusList.last().endTime) {
                return DutyStatus(dutyStatus, col, col + 1)
            }
        }
        return null
    }
}