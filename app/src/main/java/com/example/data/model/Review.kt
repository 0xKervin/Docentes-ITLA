package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val teacherId: Long,
    val authorName: String,
    val subjectName: String,
    val period: String,
    val rating: Float,
    val difficulty: Float,
    val wouldTakeAgain: Boolean,
    val attendanceMandatory: Boolean,
    val gradeReceived: String,
    val comment: String,
    val tags: String,
    val helpfulCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
