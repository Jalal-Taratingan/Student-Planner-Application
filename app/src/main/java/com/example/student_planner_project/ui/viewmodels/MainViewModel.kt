// MESSENGER OF THE REPOSITORY AND UI
package com.example.student_planner_project.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.student_planner_project.data.models.Semester
import com.example.student_planner_project.data.repository.PlannerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository : PlannerRepository) : ViewModel() {
    private val _updatedSemester = MutableStateFlow<Semester?>(null)
    val semester : StateFlow<Semester?> = _updatedSemester

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _updatedSemester.value = repository.getCurrentSemester()
        }
    }

    fun saveNewSemester( name : String, startDate : Long, endDate: Long ) {
        val newSemester = Semester(name = name, startDate = startDate, endDate = endDate)
        repository.saveSemester(newSemester)
        loadData()
    }
}

