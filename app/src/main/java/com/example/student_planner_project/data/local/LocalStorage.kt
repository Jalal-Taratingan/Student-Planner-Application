// FUNCTIONS DONE IN THE LOCAL STORAGE

package com.example.student_planner_project.data.local

import android.content.Context
import com.example.student_planner_project.data.models.Semester
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalStorageManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("student_planner_prefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    fun saveSemester(semester: Semester) {
        val jsonString = json.encodeToString(semester)
        sharedPreferences.edit().putString("saved_semester", jsonString).apply()
    }

    fun getSemester(): Semester? {
        val jsonString = sharedPreferences.getString("saved_semester", null)
        return if (jsonString != null) {
            json.decodeFromString<Semester>(jsonString)
        } else {
            null
        }
    }

    fun clearData() {
        sharedPreferences.edit().clear().apply()
    }
}