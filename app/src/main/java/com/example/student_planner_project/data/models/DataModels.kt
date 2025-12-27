// MODELS OF THE PROJECT

package com.example.student_planner_project.data.models

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Semester(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val subjects: List<Subject> = emptyList()
)

@Serializable
data class Subject(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val schedule: String,
    val professor: String? = null,
    val colorHex: String = "#F0F0F0",
    val tasks: List<Task> = emptyList()
)

@Serializable
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val dueDate: Long,
    val note: String = "",
    val isCompleted: Boolean = false,
    val isMissed: Boolean = false                           // Logic for "Missed Task" notifications
)