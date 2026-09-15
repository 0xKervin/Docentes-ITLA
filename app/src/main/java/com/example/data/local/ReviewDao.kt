package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE teacherId = :teacherId ORDER BY timestamp DESC")
    fun getReviewsForTeacher(teacherId: Long): Flow<List<Review>>

    @Query("SELECT * FROM reviews WHERE teacherId = :teacherId ORDER BY helpfulCount DESC, timestamp DESC")
    fun getReviewsForTeacherByHelpful(teacherId: Long): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<Review>)

    @Query("UPDATE reviews SET helpfulCount = helpfulCount + 1 WHERE id = :reviewId")
    suspend fun incrementHelpful(reviewId: Long)

    @Query("SELECT * FROM reviews WHERE teacherId = :teacherId")
    suspend fun getReviewsListForTeacher(teacherId: Long): List<Review>

    @Query("SELECT COUNT(*) FROM reviews")
    suspend fun countReviews(): Int
}
