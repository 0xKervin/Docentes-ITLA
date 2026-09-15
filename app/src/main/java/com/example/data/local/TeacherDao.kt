package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Teacher
import kotlinx.coroutines.flow.Flow

@Dao
interface TeacherDao {
    @Query("SELECT * FROM teachers ORDER BY averageRating DESC, reviewCount DESC")
    fun getAllTeachers(): Flow<List<Teacher>>

    @Query("SELECT * FROM teachers WHERE id = :id LIMIT 1")
    fun getTeacherById(id: Long): Flow<Teacher?>

    @Query("SELECT * FROM teachers WHERE isFavorite = 1 ORDER BY averageRating DESC")
    fun getFavoriteTeachers(): Flow<List<Teacher>>

    @Query("""
        SELECT * FROM teachers 
        WHERE name LIKE '%' || :query || '%' 
           OR subjects LIKE '%' || :query || '%' 
           OR department LIKE '%' || :query || '%'
        ORDER BY averageRating DESC
    """)
    fun searchTeachers(query: String): Flow<List<Teacher>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacher: Teacher): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeachers(teachers: List<Teacher>)

    @Update
    suspend fun updateTeacher(teacher: Teacher)

    @Query("UPDATE teachers SET isFavorite = :isFav WHERE id = :teacherId")
    suspend fun setFavorite(teacherId: Long, isFav: Boolean)

    @Query("""
        UPDATE teachers 
        SET averageRating = :avgRating, 
            difficultyRating = :diffRating, 
            wouldTakeAgainPercentage = :wouldTakeAgain, 
            reviewCount = :reviewCount, 
            topTags = :topTags 
        WHERE id = :teacherId
    """)
    suspend fun updateStats(
        teacherId: Long,
        avgRating: Double,
        diffRating: Double,
        wouldTakeAgain: Int,
        reviewCount: Int,
        topTags: String
    )

    @Query("SELECT * FROM teachers WHERE id = :id LIMIT 1")
    suspend fun getTeacherDirect(id: Long): Teacher?

    @Query("SELECT COUNT(*) FROM teachers")
    suspend fun countTeachers(): Int
}
