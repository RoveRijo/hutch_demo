package com.example.hutchsystemsdemo.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.hutchsystemsdemo.dataClasses.DutyStatus

class HomeViewModel : ViewModel() {
    // Holding a default list here to persist the data on screen rotation
    var dutyStatusList: MutableList<DutyStatus> = mutableListOf(
        DutyStatus(DutyStatus.OFF_DUTY, 5, 6),
        DutyStatus(DutyStatus.OFF_DUTY, 12, 20),
        DutyStatus(DutyStatus.SLEEPER, 6, 12),
        DutyStatus(DutyStatus.DRIVING, 1, 5),
        DutyStatus(DutyStatus.ON_DUTY, 0, 1)
    )
        get() {
            field = sortList(field)
            return field
        }

    fun addNewDutyStatus(dutyStatus: DutyStatus) {
        dutyStatusList.add(dutyStatus)
    }

    private fun sortList(list: List<DutyStatus>) = list.sortedBy { it.startTime }.toMutableList()
}