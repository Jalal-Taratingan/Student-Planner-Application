// IMPLEMENTATION OF THE FUNCTIONS THAT WILL BE USED FOR THE REPOSITORY

package com.example.student_planner_project.data.repository

import com.example.student_planner_project.data.local.LocalStorageManager
import com.example.student_planner_project.data.models.Semester
import com.example.student_planner_project.data.models.Subject
import com.example.student_planner_project.data.models.Task

class LocalPlannerRepository( private val localStorage: LocalStorageManager) : PlannerRepository {

    override fun saveNewSemester (semester: Semester){
        localStorage.saveSemester(semester)
    }

    override fun addNewSubject(subject: Subject) {
        localStorage.addSubject(subject)
    }

    override fun getCurrentSemester(): Semester? {
        return localStorage.getSemester()
    }

    override fun addNewTask(task: Task){
        localStorage.addTask(task)
    }
}