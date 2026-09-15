package com.example.data.repository

import com.example.data.local.ReviewDao
import com.example.data.local.SeedData
import com.example.data.local.TeacherDao
import com.example.data.model.Review
import com.example.data.model.Teacher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class TeacherRepository(
    private val teacherDao: TeacherDao,
    private val reviewDao: ReviewDao
) {
    val allTeachers: Flow<List<Teacher>> = teacherDao.getAllTeachers()
    val favoriteTeachers: Flow<List<Teacher>> = teacherDao.getFavoriteTeachers()

    suspend fun checkAndSeedDatabase() = withContext(Dispatchers.IO) {
        if (teacherDao.countTeachers() == 0) {
            SeedData.populateDatabase(teacherDao, reviewDao)
        }
    }

    fun getTeacherById(id: Long): Flow<Teacher?> = teacherDao.getTeacherById(id)

    fun getReviewsForTeacher(teacherId: Long): Flow<List<Review>> =
        reviewDao.getReviewsForTeacher(teacherId)

    fun searchTeachers(query: String): Flow<List<Teacher>> =
        teacherDao.searchTeachers(query)

    suspend fun toggleFavorite(teacherId: Long, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        teacherDao.setFavorite(teacherId, !isFavorite)
    }

    suspend fun incrementReviewHelpful(reviewId: Long) = withContext(Dispatchers.IO) {
        reviewDao.incrementHelpful(reviewId)
    }

    suspend fun addTeacher(teacher: Teacher): Long = withContext(Dispatchers.IO) {
        teacherDao.insertTeacher(teacher)
    }

    suspend fun addReview(review: Review): Long = withContext(Dispatchers.IO) {
        val reviewId = reviewDao.insertReview(review)
        updateTeacherStatsInternal(review.teacherId)
        reviewId
    }

    private suspend fun updateTeacherStatsInternal(teacherId: Long) {
        val allReviews = reviewDao.getReviewsListForTeacher(teacherId)
        if (allReviews.isNotEmpty()) {
            val avgRating = allReviews.map { it.rating.toDouble() }.average()
            val avgDiff = allReviews.map { it.difficulty.toDouble() }.average()
            val wouldTakeCount = allReviews.count { it.wouldTakeAgain }
            val wouldTakePercent = ((wouldTakeCount.toDouble() / allReviews.size.toDouble()) * 100).roundToInt()
            val roundedRating = (avgRating * 10).roundToInt() / 10.0
            val roundedDiff = (avgDiff * 10).roundToInt() / 10.0

            val tagFrequencies = mutableMapOf<String, Int>()
            allReviews.forEach { rev ->
                rev.tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }.forEach { tag ->
                    tagFrequencies[tag] = (tagFrequencies[tag] ?: 0) + 1
                }
            }
            val topTagsStr = tagFrequencies.entries
                .sortedByDescending { it.value }
                .take(4)
                .joinToString(", ") { it.key }

            teacherDao.updateStats(
                teacherId = teacherId,
                avgRating = roundedRating,
                diffRating = roundedDiff,
                wouldTakeAgain = wouldTakePercent,
                reviewCount = allReviews.size,
                topTags = topTagsStr
            )
        }
    }
}
