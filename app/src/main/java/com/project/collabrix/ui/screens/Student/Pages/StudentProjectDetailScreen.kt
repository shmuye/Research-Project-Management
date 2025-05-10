package com.project.collabrix.ui.screens.Student.Pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.project.collabrix.R
import com.project.collabrix.presentation.StudentProjectDetailViewModel
import com.project.collabrix.presentation.StudentProjectDetailState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentProjectDetailScreen(
    projectId: Int,
    navController: NavHostController,
    viewModel: StudentProjectDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val orbitron = FontFamily(Font(R.font.orbitron_bold))

    LaunchedEffect(projectId) {
        viewModel.loadProjectDetail(projectId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Collabrix",
                            fontFamily = orbitron,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F6FA)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F6FA)),
            contentAlignment = Alignment.TopCenter
        ) {
            when (state) {
                is StudentProjectDetailState.Loading -> {
                    CircularProgressIndicator(color = Color.Black)
                }
                is StudentProjectDetailState.Error -> {
                    Text((state as StudentProjectDetailState.Error).message, color = Color.Red, fontSize = 16.sp)
                }
                is StudentProjectDetailState.Success -> {
                    val project = (state as StudentProjectDetailState.Success).project
                    val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
                    val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
                    fun formatDate(dateStr: String): String = try {
                        LocalDate.parse(dateStr.substring(0, 10)).format(outputFormatter)
                    } catch (e: Exception) { dateStr }
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Project Title",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                            Text(
                                text = project.title,
                                fontSize = 17.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            Text(
                                text = "Project Description",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color.Black
                            )
                            Text(
                                text = project.description,
                                fontSize = 15.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            Text(
                                text = "Requirements",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color.Black
                            )
                            if (project.requirements.isEmpty()) {
                                Text("No requirements specified.", color = Color.Gray, fontSize = 15.sp)
                            } else {
                                Column {
                                    project.requirements.forEach {
                                        Text("- $it", color = Color.Black, fontSize = 15.sp)
                                    }
                                }
                            }
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            Text(
                                text = "Start Date: ${formatDate(project.startDate)}",
                                fontSize = 15.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "End Date: ${formatDate(project.endDate)}",
                                fontSize = 15.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "Deadline: ${formatDate(project.deadline)}",
                                fontSize = 15.sp,
                                color = Color.Black
                            )
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            Text(
                                text = "Project Owner (Professor)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color.Black
                            )
                            if (project.professorName != null) {
                                Text(
                                    text = project.professorName,
                                    color = Color.Black,
                                    fontSize = 15.sp
                                )
                            } else {
                                Text(
                                    text = "Professor details not available.",
                                    color = Color.Gray,
                                    fontSize = 15.sp
                                )
                            }
                            // Add info message for students
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "You will receive an email about how and when the research project will be conducted. Our team will contact you with further details.",
                                color = Color(0xFF3B82F6),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
                else -> {}
            }
        }
    }
} 