// STARTING POINT TO LOAD DATA AFTER OPENING THE APP

package com.example.student_planner_project.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.student_planner_project.data.local.LocalStorageManager
import com.example.student_planner_project.data.models.Semester
import com.example.student_planner_project.data.models.Subject
import com.example.student_planner_project.data.repository.LocalPlannerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Constructor
class MainViewModel(application : Application) : AndroidViewModel(application) {
    private val localStorage = LocalStorageManager(application)
    private val repository = LocalPlannerRepository(localStorage)

    private val _updatedSemester = MutableStateFlow<Semester?>(null)
    val semester : StateFlow<Semester?> = _updatedSemester

    val isFirstTime = MutableStateFlow(true)

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _updatedSemester.value = repository.getCurrentSemester()

            if (_updatedSemester.value  != null){
                isFirstTime.value = false
            }
        }
    }

    // Save the newly created semester
    fun saveSemester( name : String, startDate : Long, endDate: Long ) {
        val newSemester = Semester(name = name, startDate = startDate, endDate = endDate)
        repository.saveNewSemester(newSemester)
        loadData()
    }

    // Add the newly created subject
    fun addSubject(subjectName: String, subjectProfessor: String, subjectSchedule: String){
        val newSubject = Subject(name = subjectName, professor = subjectProfessor, schedule = subjectSchedule)
        repository.addNewSubject(newSubject)
        loadData()
    }

    fun deleteSubject(subject:Subject){
        val currentSemester = semester.value ?: return
        val updatedSubjects = currentSemester.subjects.filter { it != subject }
        val updatedSemester = currentSemester.copy(subjects = updatedSubjects)
        repository.saveNewSemester(updatedSemester)
        loadData()
    }

    fun finishSetup(){
        isFirstTime.value = false
    }
}

