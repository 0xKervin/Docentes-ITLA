package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Teacher
import com.example.ui.theme.ItlaCyanAccent
import com.example.ui.theme.ItlaGoldStar
import com.example.ui.theme.ItlaNavyPrimary

val AVAILABLE_TAGS = listOf(
    "Explica claro",
    "Muchos proyectos",
    "Exámenes largos",
    "Exámenes sorpresa",
    "Da oportunidad",
    "Muy puntual",
    "Clases dinámicas",
    "Laboratorios reales",
    "Exigente pero justo",
    "Accesible",
    "Usa diapositivas",
    "Mucho código",
    "Feedback detallado"
)

val GRADES_LIST = listOf("A", "B", "C", "F", "Retiré", "En curso")
val PERIODS_LIST = listOf("2025-C1", "2024-C3", "2024-C2", "2024-C1", "2023-C3")

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddReviewSheet(
    teacher: Teacher,
    onDismiss: () -> Unit,
    onSubmit: (
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
    ) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var rating by remember { mutableFloatStateOf(5.0f) }
    var difficulty by remember { mutableFloatStateOf(3.0f) }
    var subjectName by remember {
        mutableStateOf(teacher.subjects.split(",").firstOrNull()?.trim() ?: "")
    }
    var selectedPeriod by remember { mutableStateOf("2025-C1") }
    var selectedGrade by remember { mutableStateOf("A") }
    var wouldTakeAgain by remember { mutableStateOf(true) }
    var attendanceMandatory by remember { mutableStateOf(true) }
    var comment by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(true) }
    var authorName by remember { mutableStateOf("") }
    val selectedTags = remember { mutableStateListOf<String>() }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Dejar Reseña",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Para ${teacher.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Overall Rating
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¿Cómo calificas a este profesor?",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    InteractiveRatingPicker(
                        rating = rating,
                        onRatingChanged = { rating = it },
                        starSize = 38.dp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    val ratingText = when (rating.toInt()) {
                        5 -> "5.0 - ¡Excelente! Altamente recomendado"
                        4 -> "4.0 - Muy bueno, buen aprendizaje"
                        3 -> "3.0 - Promedio / Aceptable"
                        2 -> "2.0 - Deficiente, necesita mejorar"
                        else -> "1.0 - Muy mala experiencia"
                    }
                    Text(
                        text = ratingText,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Difficulty Slider
            Text(
                text = "Nivel de Dificultad: ${String.format("%.1f", difficulty)} / 5",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            val difficultyLabel = when {
                difficulty < 2.0f -> "Pase fácil / Muy baja exigencia"
                difficulty < 3.2f -> "Moderado / Exigencia justa"
                difficulty < 4.2f -> "Difícil / Requiere mucho estudio"
                else -> "Muy exigente / Parciales retadores"
            }
            Text(
                text = difficultyLabel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Slider(
                value = difficulty,
                onValueChange = { difficulty = it },
                valueRange = 1.0f..5.0f,
                steps = 7,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Subject Name
            Text(
                text = "Materia o Asignatura que tomaste",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            OutlinedTextField(
                value = subjectName,
                onValueChange = { subjectName = it },
                placeholder = { Text("Ej: Programación II, Redes CCNA...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("review_subject_input"),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            // Subject quick suggestions
            val teacherSubjectList = teacher.subjects.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            if (teacherSubjectList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    teacherSubjectList.forEach { subj ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (subjectName.equals(subj, ignoreCase = true)) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { subjectName = subj }
                        ) {
                            Text(
                                text = subj,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (subjectName.equals(subj, ignoreCase = true)) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Period & Grade Row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Período cursado",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        PERIODS_LIST.take(3).forEach { p ->
                            FilterChip(
                                selected = selectedPeriod == p,
                                onClick = { selectedPeriod = p },
                                label = { Text(p, fontSize = 11.sp) }
                            )
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nota obtenida",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        GRADES_LIST.take(4).forEach { g ->
                            FilterChip(
                                selected = selectedGrade == g,
                                onClick = { selectedGrade = g },
                                label = { Text(g, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 5. Toggles: Would take again & Mandatory attendance
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Volverías a tomar clase con él/ella?",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = wouldTakeAgain,
                        onClick = { wouldTakeAgain = true },
                        label = { Text("Sí") }
                    )
                    FilterChip(
                        selected = !wouldTakeAgain,
                        onClick = { wouldTakeAgain = false },
                        label = { Text("No") }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿La asistencia es obligatoria?",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = attendanceMandatory,
                        onClick = { attendanceMandatory = true },
                        label = { Text("Sí") }
                    )
                    FilterChip(
                        selected = !attendanceMandatory,
                        onClick = { attendanceMandatory = false },
                        label = { Text("No") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 6. Tags Selector
            Text(
                text = "Etiquetas descriptivas (selecciona las que apliquen)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                AVAILABLE_TAGS.forEach { tag ->
                    val isSelected = selectedTags.contains(tag)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                        },
                        label = { Text(tag, fontSize = 12.sp) },
                        leadingIcon = if (isSelected) {
                            { Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                        } else null
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 7. Review Comment
            Text(
                text = "Tu opinión y experiencia",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                placeholder = {
                    Text("Describe cómo explica, metodología de evaluación, exámenes, proyectos, trato con los estudiantes y consejos para pasar la materia...")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .testTag("review_comment_input"),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 8. Anonymous or Author Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = isAnonymous,
                    onCheckedChange = { isAnonymous = it }
                )
                Text(
                    text = "Publicar como anónimo (Recomendado)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (!isAnonymous) {
                OutlinedTextField(
                    value = authorName,
                    onValueChange = { authorName = it },
                    label = { Text("Tu nombre o carrera (ej. Juan - Software)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
            }

            // Error display
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = {
                    if (subjectName.isBlank()) {
                        errorMessage = "Por favor indica la asignatura que tomaste."
                        return@Button
                    }
                    if (comment.trim().length < 10) {
                        errorMessage = "Por favor escribe al menos una breve opinión (mínimo 10 caracteres)."
                        return@Button
                    }
                    val finalAuthor = if (isAnonymous || authorName.isBlank()) "Estudiante ITLA Anónimo" else authorName
                    onSubmit(
                        finalAuthor,
                        subjectName,
                        selectedPeriod,
                        rating,
                        difficulty,
                        wouldTakeAgain,
                        attendanceMandatory,
                        selectedGrade,
                        comment,
                        selectedTags
                    )
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_review_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Publicar Reseña",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
