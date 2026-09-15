package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teachers")
data class Teacher(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val department: String,
    val academicTitle: String,
    val email: String,
    val subjects: String,
    val averageRating: Double = 0.0,
    val difficultyRating: Double = 0.0,
    val wouldTakeAgainPercentage: Int = 0,
    val reviewCount: Int = 0,
    val topTags: String = "",
    val isFavorite: Boolean = false,
    val avatarColorIndex: Int = 0
)
