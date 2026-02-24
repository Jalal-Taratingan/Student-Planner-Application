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
    val units: Int,
    val schedule: String,
    val professor: String,
    val room: String,
    val tasks: List<Task> = emptyList(),
    val notes: List<Notes> = emptyList()
)

@Serializable
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val subject: Subject,
    val title: String,
    val dueDate: Long,
    val note: String = "",
    val isCompleted: Boolean = false,
    val isMissed: Boolean = false                           // Logic for "Missed Task" notifications
)

@Serializable
data class Notes(
    val id: String = UUID.randomUUID().toString(),
    val subject: Subject,
    val title: String,
    val notes: String,
    val dateCreated: Long
)