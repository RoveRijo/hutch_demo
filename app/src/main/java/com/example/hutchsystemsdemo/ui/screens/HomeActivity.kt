package com.example.hutchsystemsdemo.ui.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.viewModels
import com.example.hutchsystemsdemo.R
import com.example.hutchsystemsdemo.dataClasses.DutyStatus
import com.example.hutchsystemsdemo.ui.customViews.DutyStatusGraphView
import com.example.hutchsystemsdemo.ui.viewModels.HomeViewModel

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel by viewModels<HomeViewModel>()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val dutyStatusGraphView = DutyStatusGraphView(this, null, viewModel.dutyStatusList)
        dutyStatusGraphView.onClick = {
            //Log.d("TEST:123", it.toString())
            viewModel.addNewDutyStatus(it)
        }
        val rootView = findViewById<LinearLayout>(R.id.graphContainer)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        rootView.addView(dutyStatusGraphView, layoutParams)
    }
}