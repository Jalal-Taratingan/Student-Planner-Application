// IMPLEMENTATION OF THE FUNCTIONS THAT WILL BE USED FOR THE REPOSITORY

package com.example.student_planner_project.data.repository

import com.example.student_planner_project.data.local.LocalStorageManager
import com.example.student_planner_project.data.models.Semester
import com.example.student_planner_project.data.models.Subject
import com.example.student_planner_project.data.models.Task

class LocalPlannerRepository( private val localStorage: LocalStorageManager) : PlannerRepository {
    override fun getCurrentSemester(): Semester? = localStorage.getSemester()
    override fun saveSemester (semester: Semester){
        localStorage.saveSemester(semester)
    }
    override fun addTaskToSubject (subjectID: Subject, task: Task){
        val currentSemester = localStorage.getSemester() ?: return

        val updatedSubjects = currentSemester.subjects.map { subject ->
            if (subject == subjectID){
                subject.copy(tasks = subject.tasks + task)
            } else {
                subject
            }
        }

        val updatedSemester = currentSemester.copy(subjects = updatedSubjects)
        localStorage.saveSemester(updatedSemester)
    }
}