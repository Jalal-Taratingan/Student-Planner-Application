// DECLARATION OF THE FUNCTIONS THAT WILL BE USED FOR THE REPOSITORY

package com.example.student_planner_project.data.repository

import com.example.student_planner_project.data.models.Semester
import com.example.student_planner_project.data.models.Subject
import com.example.student_planner_project.data.models.Task

interface PlannerRepository {
    fun getCurrentSemester(): Semester?
    fun saveSemester (semester: Semester)
    fun addTaskToSubject (subjectID: Subject, task: Task)
}