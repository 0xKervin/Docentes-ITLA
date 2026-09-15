package com.example.data.local

import com.example.data.model.Review
import com.example.data.model.Teacher

object SeedData {
    suspend fun populateDatabase(teacherDao: TeacherDao, reviewDao: ReviewDao) {
        if (teacherDao.countTeachers() > 0) return

        val initialTeachers = listOf(
            Teacher(
                id = 1L,
                name = "Prof. Ramón Leonardo Ventura",
                department = "Desarrollo de Software",
                academicTitle = "M.Sc. en Ingeniería de Software",
                email = "r.ventura@itla.edu.do",
                subjects = "Programación I, Programación II, C# .NET, POO",
                averageRating = 4.8,
                difficultyRating = 3.3,
                wouldTakeAgainPercentage = 95,
                reviewCount = 14,
                topTags = "Explica claro, Proyectos prácticos, Muy puntual, Da oportunidad",
                isFavorite = true,
                avatarColorIndex = 0
            ),
            Teacher(
                id = 2L,
                name = "Ing. Patricia Gómez Reyes",
                department = "Desarrollo de Software",
                academicTitle = "Especialista en Arquitectura de Software",
                email = "p.gomez@itla.edu.do",
                subjects = "Estructura de Datos, Patrones de Diseño, Algoritmos Avanzados",
                averageRating = 4.6,
                difficultyRating = 4.2,
                wouldTakeAgainPercentage = 89,
                reviewCount = 11,
                topTags = "Mucho código, Exigente pero justa, Explica a fondo, Laboratorios retadores",
                isFavorite = false,
                avatarColorIndex = 1
            ),
            Teacher(
                id = 3L,
                name = "Ing. Marcos Antonio Rosario",
                department = "Redes e Infraestructura",
                academicTitle = "Certificado Cisco CCIE / M.Sc. Telecomunicaciones",
                email = "m.rosario@itla.edu.do",
                subjects = "Redes CCNA I, Enrutamiento y Conmutación, Servidores Linux",
                averageRating = 4.9,
                difficultyRating = 3.1,
                wouldTakeAgainPercentage = 98,
                reviewCount = 18,
                topTags = "El mejor de redes, Clases dinámicas, Laboratorios con Packet Tracer, Accesible",
                isFavorite = true,
                avatarColorIndex = 2
            ),
            Teacher(
                id = 4L,
                name = "Lic. Félix Castillo Peña",
                department = "Ciencias Básicas",
                academicTitle = "Lic. en Matemáticas Puras / Magíster en Docencia",
                email = "f.castillo@itla.edu.do",
                subjects = "Cálculo Diferencial, Cálculo Integral, Álgebra Lineal",
                averageRating = 4.2,
                difficultyRating = 4.6,
                wouldTakeAgainPercentage = 78,
                reviewCount = 16,
                topTags = "Exámenes difíciles, Tareas largas, Explica bien, Hace muchas preguntas",
                isFavorite = false,
                avatarColorIndex = 3
            ),
            Teacher(
                id = 5L,
                name = "Dra. Vanessa Morales Cruz",
                department = "Inteligencia Artificial",
                academicTitle = "Ph.D. en Ciencias de la Computación / IA",
                email = "v.morales@itla.edu.do",
                subjects = "Machine Learning, Deep Learning, Python para Ciencia de Datos",
                averageRating = 4.7,
                difficultyRating = 3.9,
                wouldTakeAgainPercentage = 93,
                reviewCount = 9,
                topTags = "Muy actualizada, Proyectos con IA real, Guía paso a paso, Excelente docente",
                isFavorite = false,
                avatarColorIndex = 4
            ),
            Teacher(
                id = 6L,
                name = "Ing. Héctor David Silvestre",
                department = "Ciberseguridad",
                academicTitle = "Certified Ethical Hacker (CEH) / CISSP",
                email = "h.silvestre@itla.edu.do",
                subjects = "Hacking Ético, Seguridad Ofensiva, Análisis Forense Digital",
                averageRating = 4.5,
                difficultyRating = 4.1,
                wouldTakeAgainPercentage = 87,
                reviewCount = 8,
                topTags = "CTFs en clase, Prácticas reales, Muy exigente, Aprenderás mucho",
                isFavorite = false,
                avatarColorIndex = 5
            ),
            Teacher(
                id = 7L,
                name = "Licda. Ana Sofía Tejada",
                department = "Multimedia",
                academicTitle = "Diseñadora Interactiva & Magíster UX/UI",
                email = "a.tejada@itla.edu.do",
                subjects = "Diseño de Interfaces UI/UX, Prototipado en Figma, Diseño Gráfico",
                averageRating = 4.9,
                difficultyRating = 2.7,
                wouldTakeAgainPercentage = 96,
                reviewCount = 12,
                topTags = "Feedback constructivo, Muy creativa, Califica a tiempo, Clases interactivas",
                isFavorite = false,
                avatarColorIndex = 6
            ),
            Teacher(
                id = 8L,
                name = "Ing. Manuel Emilio Santana",
                department = "Mecatrónica",
                academicTitle = "Ing. Mecatrónico / Especialista en Automatización",
                email = "m.santana@itla.edu.do",
                subjects = "Microcontroladores, Robótica Móvil, PLCs y Automatización",
                averageRating = 4.4,
                difficultyRating = 4.0,
                wouldTakeAgainPercentage = 84,
                reviewCount = 7,
                topTags = "Mucho taller, Proyectos con Arduino/ESP32, Exámenes teóricos fuertes",
                isFavorite = false,
                avatarColorIndex = 7
            )
        )

        teacherDao.insertTeachers(initialTeachers)

        val initialReviews = listOf(
            // Reviews for Ramon Ventura (Teacher 1)
            Review(
                teacherId = 1L,
                authorName = "Estudiante de Software (2023-XXXX)",
                subjectName = "Programación II",
                period = "2024-C3",
                rating = 5.0f,
                difficulty = 3.0f,
                wouldTakeAgain = true,
                attendanceMandatory = true,
                gradeReceived = "A",
                comment = "Excelente profesor. Tiene una pedagogía increíble para explicar programación orientada a objetos. Deja un proyecto final retador pero muy formativo con base de datos SQL y C#. Si haces las prácticas a tiempo pasas con buena nota.",
                tags = "Explica claro, Proyectos prácticos, Muy puntual",
                helpfulCount = 15,
                timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 12
            ),
            Review(
                teacherId = 1L,
                authorName = "Carlos R.",
                subjectName = "Programación I",
                period = "2024-C2",
                rating = 5.0f,
                difficulty = 3.5f,
                wouldTakeAgain = true,
                attendanceMandatory = true,
                gradeReceived = "A",
                comment = "Si vienes desde cero en lógica de programación, con Ventura vas a entender todo. Es súper respetuoso, responde dudas en Canvas y en el aula. 100% recomendado.",
                tags = "Da oportunidad, Explica claro, Clases dinámicas",
                helpfulCount = 9,
                timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 45
            ),
            Review(
                teacherId = 1L,
                authorName = "Anónimo ITLA",
                subjectName = "POO Avanzada",
                period = "2024-C1",
                rating = 4.5f,
                difficulty = 3.5f,
                wouldTakeAgain = true,
                attendanceMandatory = false,
                gradeReceived = "B",
                comment = "Muy buena metodología. Sus asignaciones toman tiempo así que no lo dejes para la última semana. Pone ejemplos del mundo laboral dominicano y extranjero.",
                tags = "Mucho código, Puntual",
                helpfulCount = 4,
                timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 90
            ),

            // Reviews for Patricia Gomez (Teacher 2)
            Review(
                teacherId = 2L,
                authorName = "Dev Junior ITLA",
                subjectName = "Estructura de Datos",
                period = "2024-C3",
                rating = 5.0f,
                difficulty = 4.5f,
                wouldTakeAgain = true,
                attendanceMandatory = true,
                gradeReceived = "B",
                comment = "Es exigente, no te va a regalar nada. Pero sales sabiendo árboles binarios, grafos y complejidad algorítmica Big-O como un profesional. Prepárate para estudiar en serio.",
                tags = "Exigente pero justa, Explica a fondo, Laboratorios retadores",
                helpfulCount = 18,
                timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 10
            ),
            Review(
                teacherId = 2L,
                authorName = "Miguel A.",
                subjectName = "Patrones de Diseño",
                period = "2024-C2",
                rating = 4.0f,
                difficulty = 4.0f,
                wouldTakeAgain = true,
                attendanceMandatory = true,
                gradeReceived = "A",
                comment = "La materia es pesada pero la profesora domina el tema al 100%. Te corrige el código línea por línea en GitHub y te enseña buenas prácticas de arquitectura limpia.",
                tags = "Mucho código, Exámenes difíciles",
                helpfulCount = 7,
                timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 60
            ),

            // Reviews for Marcos Rosario (Teacher 3)
            Review(
                teacherId = 3L,
                authorName = "Estudiante Redes (2022-XXXX)",
                subjectName = "Redes CCNA I",
                period = "2024-C3",
                rating = 5.0f,
                difficulty = 3.0f,
                wouldTakeAgain = true,
                attendanceMandatory = true,
                gradeReceived = "A",
                comment = "El mejor maestro de redes del ITLA sin duda alguna. Explica subredes y VLSM de una forma tan simple que nadie se queda con dudas. Los laboratorios con racks y switches reales son lo mejor.",
                tags = "El mejor de redes, Clases dinámicas, Accesible",
                helpfulCount = 24,
                timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 8
            ),
            Review(
                teacherId = 3L,
                authorName = "Yariel S.",
                subjectName = "Enrutamiento y Conmutación",
                period = "2024-C2",
                rating = 5.0f,
                difficulty = 3.2f,
                wouldTakeAgain = true,
                attendanceMandatory = false,
                gradeReceived = "A",
                comment = "Muy dedicado a sus alumnos. Si le pides ayuda fuera de horario te apoya. Gran persona y excelente profesional.",
                tags = "Muy puntual, Da oportunidad, Accesible",
                helpfulCount = 11,
                timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 70
            ),

            // Reviews for Felix Castillo (Teacher 4)
            Review(
                teacherId = 4L,
                authorName = "Estudiante de Ciberseguridad",
                subjectName = "Cálculo Integral",
                period = "2024-C3",
                rating = 4.0f,
                difficulty = 4.8f,
                wouldTakeAgain = true,
                attendanceMandatory = true,
                gradeReceived = "C",
                comment = "Explica muy bien en la pizarra paso por paso, pero sus parciales son súper largos y complicados. Tienes que practicar todos los ejercicios del Stewart si quieres pasar.",
                tags = "Exámenes difíciles, Tareas largas, Explica bien",
                helpfulCount = 14,
                timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 15
            ),
            Review(
                teacherId = 4L,
                authorName = "Anónimo",
                subjectName = "Cálculo Diferencial",
                period = "2024-C1",
                rating = 4.5f,
                difficulty = 4.4f,
                wouldTakeAgain = true,
                attendanceMandatory = true,
                gradeReceived = "B",
                comment = "Castillo ama las matemáticas y eso se nota. No falta a clase nunca y es muy puntual. Da puntos por participación en la pizarra.",
                tags = "Muy puntual, Hace muchas preguntas, Da oportunidad",
                helpfulCount = 6,
                timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 110
            ),

            // Reviews for Vanessa Morales (Teacher 5)
            Review(
                teacherId = 5L,
                authorName = "Data Science Enthusiast",
                subjectName = "Machine Learning",
                period = "2024-C3",
                rating = 5.0f,
                difficulty = 3.8f,
                wouldTakeAgain = true,
                attendanceMandatory = false,
                gradeReceived = "A",
                comment = "Profesora de nivel internacional. Aprendimos Scikit-Learn, Pandas y redes neuronales con PyTorch aplicadas a casos reales. La materia de mayor provecho en la carrera de IA.",
                tags = "Muy actualizada, Proyectos con IA real, Excelente docente",
                helpfulCount = 13,
                timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 20
            ),

            // Reviews for Hector Silvestre (Teacher 6)
            Review(
                teacherId = 6L,
                authorName = "CiberSec ITLA",
                subjectName = "Hacking Ético",
                period = "2024-C3",
                rating = 4.5f,
                difficulty = 4.2f,
                wouldTakeAgain = true,
                attendanceMandatory = true,
                gradeReceived = "A",
                comment = "Las clases son con máquinas virtuales en HackTheBox y TryHackMe. Tienes que documentar cada prueba con capturas y pasos metodológicos. Muy profesional.",
                tags = "CTFs en clase, Prácticas reales, Muy exigente",
                helpfulCount = 10,
                timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 14
            ),

            // Reviews for Ana Sofia Tejada (Teacher 7)
            Review(
                teacherId = 7L,
                authorName = "Diseñador Multimedia",
                subjectName = "Diseño de Interfaces UI/UX",
                period = "2024-C3",
                rating = 5.0f,
                difficulty = 2.8f,
                wouldTakeAgain = true,
                attendanceMandatory = false,
                gradeReceived = "A",
                comment = "Increíble docente. Aprendimos design systems en Figma, tests de usabilidad y arquitectura de información. Clases muy amenas y siempre da feedback detallado para mejorar los proyectos.",
                tags = "Feedback constructivo, Muy creativa, Califica a tiempo",
                helpfulCount = 12,
                timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 25
            )
        )

        reviewDao.insertReviews(initialReviews)
    }
}
