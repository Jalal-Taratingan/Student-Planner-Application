// FUNCTIONS DONE IN THE LOCAL STORAGE

package com.example.student_planner_project.data.local

import android.content.Context
import com.example.student_planner_project.data.models.Semester
import com.example.student_planner_project.data.models.Subject
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

//    fun addSubject(subject: Subject){
//        val currentSubjects = getSubjects().toMutableList()
//        currentSubjects.add(subject)
//
//        val jsonString = json.encodeToString(currentSubjects)
//        sharedPreferences.edit().putString("saved_subject", jsonString).apply()
//    }
//
//    fun getSubjects(): List<Subject>{
//        val jsonString = sharedPreferences.getString("saved_subject", null)
//        return if (jsonString != null) {
//            json.decodeFromString<List<Subject>>(jsonString)
//        } else {
//            emptyList()
//        }
//    }

    fun clearData() {
        sharedPreferences.edit().clear().apply()
    }
}