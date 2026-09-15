package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ItlaDatabase
import com.example.data.model.Review
import com.example.data.model.Teacher
import com.example.data.repository.TeacherRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class TeacherSortOption(val label: String) {
    RATING_DESC("Mejor Calificados"),
    REVIEWS_DESC("Más Reseñas"),
    DIFFICULTY_ASC("Menor Dificultad"),
    DIFFICULTY_DESC("Mayor Dificultad"),
    NAME_ASC("Nombre (A-Z)")
}

enum class ReviewSortOption(val label: String) {
    NEWEST("Más Recientes"),
    HELPFUL("Más Útiles"),
    RATING_HIGH("Mejor Nota"),
    RATING_LOW("Menor Nota")
}

data class TeacherUiState(
    val teachers: List<Teacher> = emptyList(),
    val filteredTeachers: List<Teacher> = emptyList(),
    val searchQuery: String = "",
    val selectedDepartment: String = "Todos",
    val sortOption: TeacherSortOption = TeacherSortOption.RATING_DESC,
    val showOnlyFavorites: Boolean = false,
    val selectedTeacherId: Long? = null,
    val isSeeding: Boolean = false
)

class TeacherViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TeacherRepository

    private val _uiState = MutableStateFlow(TeacherUiState())
    val uiState: StateFlow<TeacherUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    private val _reviewSortOption = MutableStateFlow(ReviewSortOption.NEWEST)
    val reviewSortOption: StateFlow<ReviewSortOption> = _reviewSortOption.asStateFlow()

    val currentTeacher: StateFlow<Teacher?>
    val currentReviews: StateFlow<List<Review>>

    init {
        val database = ItlaDatabase.getInstance(application, viewModelScope)
        repository = TeacherRepository(database.teacherDao(), database.reviewDao())

        viewModelScope.launch {
            repository.checkAndSeedDatabase()
        }

        viewModelScope.launch {
            combine(
                repository.allTeachers,
                _uiState
            ) { teachers, state ->
                val filtered = teachers.filter { teacher ->
                    val matchesSearch = if (state.searchQuery.isBlank()) {
                        true
                    } else {
                        val q = state.searchQuery.trim().lowercase()
                        teacher.name.lowercase().contains(q) ||
                                teacher.subjects.lowercase().contains(q) ||
                                teacher.department.lowercase().contains(q) ||
                                teacher.topTags.lowercase().contains(q)
                    }

                    val matchesDept = if (state.selectedDepartment == "Todos") {
                        true
                    } else {
                        teacher.department.contains(state.selectedDepartment, ignoreCase = true)
                    }

                    val matchesFav = if (state.showOnlyFavorites) teacher.isFavorite else true

                    matchesSearch && matchesDept && matchesFav
                }.sortedWith { a, b ->
                    when (state.sortOption) {
                        TeacherSortOption.RATING_DESC -> b.averageRating.compareTo(a.averageRating)
                        TeacherSortOption.REVIEWS_DESC -> b.reviewCount.compareTo(a.reviewCount)
                        TeacherSortOption.DIFFICULTY_ASC -> a.difficultyRating.compareTo(b.difficultyRating)
                        TeacherSortOption.DIFFICULTY_DESC -> b.difficultyRating.compareTo(a.difficultyRating)
                        TeacherSortOption.NAME_ASC -> a.name.compareTo(b.name, ignoreCase = true)
                    }
                }

                _uiState.value = _uiState.value.copy(
                    teachers = teachers,
                    filteredTeachers = filtered
                )
            }.collect {}
        }

        currentTeacher = _uiState.flatMapLatest { state ->
            val id = state.selectedTeacherId
            if (id != null) {
                repository.getTeacherById(id)
            } else {
                flowOf(null)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

        currentReviews = combine(
            _uiState.flatMapLatest { state ->
                val id = state.selectedTeacherId
                if (id != null) repository.getReviewsForTeacher(id) else flowOf(emptyList())
            },
            _reviewSortOption
        ) { reviews, sort ->
            when (sort) {
                ReviewSortOption.NEWEST -> reviews.sortedByDescending { it.timestamp }
                ReviewSortOption.HELPFUL -> reviews.sortedWith(
                    compareByDescending<Review> { it.helpfulCount }.thenByDescending { it.timestamp }
                )
                ReviewSortOption.RATING_HIGH -> reviews.sortedByDescending { it.rating }
                ReviewSortOption.RATING_LOW -> reviews.sortedBy { it.rating }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        recomputeFiltered()
    }

    fun onDepartmentSelected(dept: String) {
        _uiState.value = _uiState.value.copy(selectedDepartment = dept)
        recomputeFiltered()
    }

    fun onSortOptionChanged(option: TeacherSortOption) {
        _uiState.value = _uiState.value.copy(sortOption = option)
        recomputeFiltered()
    }

    fun toggleFavoritesOnly() {
        _uiState.value = _uiState.value.copy(showOnlyFavorites = !_uiState.value.showOnlyFavorites)
        recomputeFiltered()
    }

    fun selectTeacher(teacherId: Long?) {
        _uiState.value = _uiState.value.copy(selectedTeacherId = teacherId)
    }

    fun setReviewSortOption(sortOption: ReviewSortOption) {
        _reviewSortOption.value = sortOption
    }

    private fun recomputeFiltered() {
        val state = _uiState.value
        val filtered = state.teachers.filter { teacher ->
            val matchesSearch = if (state.searchQuery.isBlank()) {
                true
            } else {
                val q = state.searchQuery.trim().lowercase()
                teacher.name.lowercase().contains(q) ||
                        teacher.subjects.lowercase().contains(q) ||
                        teacher.department.lowercase().contains(q) ||
                        teacher.topTags.lowercase().contains(q)
            }

            val matchesDept = if (state.selectedDepartment == "Todos") {
                true
            } else {
                teacher.department.contains(state.selectedDepartment, ignoreCase = true)
            }

            val matchesFav = if (state.showOnlyFavorites) teacher.isFavorite else true

            matchesSearch && matchesDept && matchesFav
        }.sortedWith { a, b ->
            when (state.sortOption) {
                TeacherSortOption.RATING_DESC -> b.averageRating.compareTo(a.averageRating)
                TeacherSortOption.REVIEWS_DESC -> b.reviewCount.compareTo(a.reviewCount)
                TeacherSortOption.DIFFICULTY_ASC -> a.difficultyRating.compareTo(b.difficultyRating)
                TeacherSortOption.DIFFICULTY_DESC -> b.difficultyRating.compareTo(a.difficultyRating)
                TeacherSortOption.NAME_ASC -> a.name.compareTo(b.name, ignoreCase = true)
            }
        }
        _uiState.value = state.copy(filteredTeachers = filtered)
    }

    fun toggleFavorite(teacher: Teacher) {
        viewModelScope.launch {
            repository.toggleFavorite(teacher.id, teacher.isFavorite)
            val msg = if (!teacher.isFavorite) "Añadido a favoritos" else "Eliminado de favoritos"
            _snackbarMessage.emit(msg)
        }
    }

    fun markReviewHelpful(reviewId: Long) {
        viewModelScope.launch {
            repository.incrementReviewHelpful(reviewId)
            _snackbarMessage.emit("¡Gracias por tu valoración!")
        }
    }

    fun submitReview(
        teacherId: Long,
        authorName: String,
        subjectName: String,
        period: String,
        rating: Float,
        difficulty: Float,
        wouldTakeAgain: Boolean,
        attendanceMandatory: Boolean,
        gradeReceived: String,
        comment: String,
        tags: List<String>
    ) {
        viewModelScope.launch {
            val finalAuthor = if (authorName.isBlank()) "Estudiante ITLA Anónimo" else authorName.trim()
            val newReview = Review(
                teacherId = teacherId,
                authorName = finalAuthor,
                subjectName = subjectName.trim(),
                period = period.trim(),
                rating = rating,
                difficulty = difficulty,
                wouldTakeAgain = wouldTakeAgain,
                attendanceMandatory = attendanceMandatory,
                gradeReceived = gradeReceived,
                comment = comment.trim(),
                tags = tags.joinToString(", "),
                helpfulCount = 0,
                timestamp = System.currentTimeMillis()
            )
            repository.addReview(newReview)
            _snackbarMessage.emit("¡Reseña publicada con éxito!")
        }
    }

    fun addNewTeacher(
        name: String,
        department: String,
        academicTitle: String,
        email: String,
        subjects: String,
        initialRating: Float?,
        initialComment: String?
    ) {
        viewModelScope.launch {
            val colorIdx = (name.hashCode().coerceAtLeast(0)) % 8
            val newTeacher = Teacher(
                name = name.trim(),
                department = department.trim(),
                academicTitle = if (academicTitle.isBlank()) "Docente ITLA" else academicTitle.trim(),
                email = if (email.isBlank()) "info@itla.edu.do" else email.trim(),
                subjects = subjects.trim(),
                averageRating = initialRating?.toDouble() ?: 0.0,
                difficultyRating = 3.0,
                wouldTakeAgainPercentage = 100,
                reviewCount = if (initialRating != null) 1 else 0,
                topTags = "Nuevo docente",
                isFavorite = false,
                avatarColorIndex = colorIdx
            )
            val teacherId = repository.addTeacher(newTeacher)

            if (initialRating != null && !initialComment.isNullOrBlank()) {
                val review = Review(
                    teacherId = teacherId,
                    authorName = "Estudiante ITLA",
                    subjectName = subjects.split(",").firstOrNull()?.trim() ?: "Asignatura",
                    period = "2025-C1",
                    rating = initialRating,
                    difficulty = 3.0f,
                    wouldTakeAgain = true,
                    attendanceMandatory = true,
                    gradeReceived = "A",
                    comment = initialComment.trim(),
                    tags = "Recomendado",
                    helpfulCount = 1
                )
                repository.addReview(review)
            }

            _snackbarMessage.emit("¡Profesor $name agregado al catálogo!")
        }
    }
}
