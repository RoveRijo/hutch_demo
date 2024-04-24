package com.example.hutchsystemsdemo.ui.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.hutchsystemsdemo.R
import com.example.hutchsystemsdemo.dataClasses.DutyStatus
import com.example.hutchsystemsdemo.ui.customViews.DutyStatusGraphView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val dutyStatusGraphView = DutyStatusGraphView(this, null)
        val rootView = findViewById<LinearLayout>(R.id.graphContainer)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        rootView.addView(dutyStatusGraphView, layoutParams)
    }
}