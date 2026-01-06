// FUNCTIONS DONE IN THE LOCAL STORAGE

package com.example.student_planner_project.data.local

import android.content.Context
import com.example.student_planner_project.data.models.Notes
import com.example.student_planner_project.data.models.Semester
import com.example.student_planner_project.data.models.Subject
import com.example.student_planner_project.data.models.Task
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalStorageManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("student_planner_prefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    // Save the semester
    fun saveSemester(semester: Semester) {
        val jsonString = json.encodeToString(semester)
        sharedPreferences.edit().putString("saved_semester", jsonString).apply()
    }

    // Get the semester
    fun getSemester(): Semester? {
        val jsonString = sharedPreferences.getString("saved_semester", null)
        return if (jsonString != null) {
            json.decodeFromString<Semester>(jsonString)
        } else {
            null
        }
    }

    // Add a subject to the list
    fun addSubject(subject: Subject){
        val currentSemester = getSemester() ?: return
        val updatedSubjects = currentSemester.subjects + subject
        val updatedSemester = currentSemester.copy(subjects = updatedSubjects)
        saveSemester(updatedSemester)
    }

    // Add a task
    fun addTask(task: Task){
        val currentSemester = getSemester() ?: return
        val updatedSubjects = currentSemester.subjects.map{ subject ->
            if(subject.id == task.subject.id){
                val updatedTasks = subject.tasks + task
                subject.copy(tasks = updatedTasks)
            }else{
                subject
            }
        }
        val updatedSemester = currentSemester.copy(subjects = updatedSubjects)
        saveSemester(updatedSemester)
    }

    // Add a note
    fun addNotes(notes: Notes){
        val currentSemester = getSemester() ?: return
        val updatedSubjects = currentSemester.subjects.map{ subject ->
            if(subject.id == notes.subject.id){
                val updatedNotes = subject.notes + notes
                subject.copy(notes = updatedNotes)
            }else{
                subject
            }
        }
        val updatedSemester = currentSemester.copy(subjects = updatedSubjects)
        saveSemester(updatedSemester)
    }

    fun clearData() {
        sharedPreferences.edit().clear().apply()
    }
}