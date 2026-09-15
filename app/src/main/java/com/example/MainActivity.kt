package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.TeacherDetailScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.TeacherViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
  private val viewModel: TeacherViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val currentTeacher by viewModel.currentTeacher.collectAsStateWithLifecycle()
        val currentReviews by viewModel.currentReviews.collectAsStateWithLifecycle()
        val reviewSortOption by viewModel.reviewSortOption.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
          viewModel.snackbarMessage.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
          }
        }

        BackHandler(enabled = uiState.selectedTeacherId != null) {
          viewModel.selectTeacher(null)
        }

        Scaffold(
          modifier = Modifier.fillMaxSize(),
          snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
          AnimatedContent(
            targetState = uiState.selectedTeacherId,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "screen_transition"
          ) { teacherId ->
            if (teacherId == null || currentTeacher == null) {
              HomeScreen(
                uiState = uiState,
                onSearchChanged = viewModel::onSearchQueryChanged,
                onDepartmentSelected = viewModel::onDepartmentSelected,
                onSortChanged = viewModel::onSortOptionChanged,
                onToggleFavoritesOnly = viewModel::toggleFavoritesOnly,
                onTeacherSelected = viewModel::selectTeacher,
                onToggleFavorite = viewModel::toggleFavorite,
                onAddNewTeacher = viewModel::addNewTeacher,
                modifier = Modifier.padding(innerPadding)
              )
            } else {
              TeacherDetailScreen(
                teacher = currentTeacher!!,
                reviews = currentReviews,
                reviewSortOption = reviewSortOption,
                onBack = { viewModel.selectTeacher(null) },
                onToggleFavorite = { viewModel.toggleFavorite(currentTeacher!!) },
                onReviewSortChanged = viewModel::setReviewSortOption,
                onMarkHelpful = viewModel::markReviewHelpful,
                onSubmitReview = { author, subj, period, rating, diff, wouldTake, attend, grade, comment, tags ->
                  viewModel.submitReview(
                    teacherId = currentTeacher!!.id,
                    authorName = author,
                    subjectName = subj,
                    period = period,
                    rating = rating,
                    difficulty = diff,
                    wouldTakeAgain = wouldTake,
                    attendanceMandatory = attend,
                    gradeReceived = grade,
                    comment = comment,
                    tags = tags
                  )
                },
                modifier = Modifier.padding(innerPadding)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  MyApplicationTheme { Greeting("Android") }
}
