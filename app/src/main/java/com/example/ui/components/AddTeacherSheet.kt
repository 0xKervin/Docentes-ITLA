package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val ITLA_DEPARTMENTS = listOf(
    "Desarrollo de Software",
    "Redes e Infraestructura",
    "Ciberseguridad",
    "Ciencias Básicas",
    "Inteligencia Artificial",
    "Mecatrónica",
    "Multimedia"
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddTeacherSheet(
    onDismiss: () -> Unit,
    onSubmit: (
        name: String,
        department: String,
        academicTitle: String,
        email: String,
        subjects: String,
        initialRating: Float?,
        initialComment: String?
    ) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var name by remember { mutableStateOf("") }
    var department by remember { mutableStateOf(ITLA_DEPARTMENTS.first()) }
    var academicTitle by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var subjects by remember { mutableStateOf("") }
    var addReviewNow by remember { mutableStateOf(false) }
    var initialRating by remember { mutableFloatStateOf(5.0f) }
    var initialComment by remember { mutableStateOf("") }

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
                        text = "Agregar Nuevo Docente",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Añade un profesor del ITLA para que otros puedan evaluarlo",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Teacher Name
            Text(
                text = "Nombre y Apellidos del Profesor *",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Ej: Ing. Pedro Santana o Licda. María Pérez") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_teacher_name_input"),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Department selector
            Text(
                text = "Área o Carrera del ITLA *",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ITLA_DEPARTMENTS.forEach { dept ->
                    FilterChip(
                        selected = department == dept,
                        onClick = { department = dept },
                        label = { Text(dept, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Academic Title
            Text(
                text = "Título Académico o Especialidad",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            OutlinedTextField(
                value = academicTitle,
                onValueChange = { academicTitle = it },
                placeholder = { Text("Ej: M.Sc. en Seguridad Informática, Ing. en Sistemas") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Email
            Text(
                text = "Correo Institucional ITLA (Opcional)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("ejemplo@itla.edu.do") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Subjects taught
            Text(
                text = "Asignaturas que imparte * (separadas por comas)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            OutlinedTextField(
                value = subjects,
                onValueChange = { subjects = it },
                placeholder = { Text("Ej: Programación Web, Bases de Datos, Algoritmos") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_teacher_subjects_input"),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Optional First Review
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¿Deseas dejar su primera reseña ahora?",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                FilterChip(
                    selected = addReviewNow,
                    onClick = { addReviewNow = !addReviewNow },
                    label = { Text(if (addReviewNow) "Sí" else "No") }
                )
            }

            if (addReviewNow) {
                Spacer(modifier = Modifier.height(10.dp))
                InteractiveRatingPicker(
                    rating = initialRating,
                    onRatingChanged = { initialRating = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = initialComment,
                    onValueChange = { initialComment = it },
                    placeholder = { Text("Escribe tu comentario sobre este docente...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (name.trim().length < 3) {
                        errorMessage = "Por favor ingresa un nombre válido para el docente."
                        return@Button
                    }
                    if (subjects.trim().isBlank()) {
                        errorMessage = "Por favor indica al menos una asignatura que imparta."
                        return@Button
                    }
                    onSubmit(
                        name,
                        department,
                        academicTitle,
                        email,
                        subjects,
                        if (addReviewNow) initialRating else null,
                        if (addReviewNow) initialComment else null
                    )
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_teacher_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Registrar Docente",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
