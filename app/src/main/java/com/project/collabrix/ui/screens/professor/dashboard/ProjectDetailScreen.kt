package com.project.collabrix.ui.screens.professor.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import com.project.collabrix.presentation.main.ProjectDetailViewModel
import com.project.collabrix.presentation.main.ProjectDetailState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    projectId: Int,
    navController: NavHostController,
    viewModel: ProjectDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
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
                is ProjectDetailState.Loading -> {
                    CircularProgressIndicator(color = Color.Black)
                }
                is ProjectDetailState.Error -> {
                    Text((state as ProjectDetailState.Error).message, color = Color.Red, fontSize = 16.sp)
                }
                is ProjectDetailState.Deleted -> {
                    // Navigate back after deletion
                    LaunchedEffect(Unit) { navController.popBackStack() }
                }
                is ProjectDetailState.Success -> {
                    val project = (state as ProjectDetailState.Success).project
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
                            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                                project.requirements.forEach {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(checked = false, onCheckedChange = null)
                                        Text(text = it, fontSize = 15.sp)
                                    }
                                }
                            }
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Start Date", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text(formatDate(project.startDate), fontSize = 15.sp)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("End Date", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text(formatDate(project.endDate), fontSize = 15.sp)
                                }
                            }
                            Text("Deadline", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(formatDate(project.deadline), fontSize = 15.sp, modifier = Modifier.padding(bottom = 12.dp))
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            Text("Students", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color.Black)
                            if (project.students.orEmpty().isEmpty()) {
                                Text("No students have joined this project yet.", color = Color.Gray, fontSize = 15.sp, modifier = Modifier.padding(vertical = 8.dp))
                            } else {
                                Column {
                                    project.students.orEmpty().forEach { student ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                        ) {
                                            Text(student.name, fontSize = 15.sp, modifier = Modifier.weight(1f))
                                            IconButton(onClick = {
                                                scope.launch {
                                                    viewModel.removeStudent(projectId, student.id)
                                                }
                                            }) {
                                                Icon(Icons.Default.Delete, contentDescription = "Remove Student", tint = Color.Red)
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = {
                                    scope.launch { viewModel.deleteProject(projectId) }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Project", tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Delete Project", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
} 