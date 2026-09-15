package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.Review
import com.example.data.model.Teacher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Teacher::class, Review::class], version = 1, exportSchema = false)
abstract class ItlaDatabase : RoomDatabase() {
    abstract fun teacherDao(): TeacherDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: ItlaDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): ItlaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItlaDatabase::class.java,
                    "itla_professors.db"
                ).addCallback(DatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        SeedData.populateDatabase(database.teacherDao(), database.reviewDao())
                    }
                }
            }
        }
    }
}
