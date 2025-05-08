package com.project.collabrix.ui.screens.main.professor.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.collabrix.R
import com.project.collabrix.data.dto.CreateProjectRequest
import com.project.collabrix.data.repository.ProjectRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Image
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.res.fontResource
import com.project.collabrix.ui.screens.main.professor.dashboard.ProjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(onBack: () -> Unit) {
    val orbitron = FontFamily(Font(R.font.orbitron_bold))
    val viewModel: ProjectViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var requirements by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var success by remember { mutableStateOf(false) }

    // Date format for backend
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(modifier = Modifier.fillMaxWidth().padding(0.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(0.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = onBack) {
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
                            Image(
                                painter = painterResource(id = R.drawable.app_logo),
                                contentDescription = "App Logo",
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.fillMaxWidth().padding(0.dp))
                    }
                },
                navigationIcon = {},
                actions = {},
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Create New Research Project", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Post a new Research opportunity for students", color = Color.Gray, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    border = ButtonDefaults.outlinedButtonBorder,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Project Details", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                        Text("Basic information about your reasearch project", color = Color.Gray, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Enter title") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Project Description", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Enter description") },
                            modifier = Modifier.fillMaxWidth().height(100.dp),
                            maxLines = 5
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Requirements", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                        OutlinedTextField(
                            value = requirements,
                            onValueChange = { requirements = it },
                            label = { Text("Enter requirements") },
                            modifier = Modifier.fillMaxWidth().height(70.dp),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Start date", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                                OutlinedTextField(
                                    value = startDate,
                                    onValueChange = { startDate = it },
                                    label = { Text("mm/dd/yyyy") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("End date", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                                OutlinedTextField(
                                    value = endDate,
                                    onValueChange = { endDate = it },
                                    label = { Text("mm/dd/yyyy") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Application Deadline", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                        OutlinedTextField(
                            value = deadline,
                            onValueChange = { deadline = it },
                            label = { Text("mm/dd/yyyy") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        if (error != null) {
                            Text(error!!, color = Color.Red, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Button(
                            onClick = {
                                error = null
                                val reqList = requirements.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                                if (title.isBlank() || description.isBlank() || reqList.isEmpty() || startDate.isBlank() || endDate.isBlank() || deadline.isBlank()) {
                                    error = "All fields are required and requirements must not be empty."
                                    return@Button
                                }
                                loading = true
                                val req = CreateProjectRequest(
                                    title = title,
                                    description = description,
                                    requirements = reqList,
                                    startDate = parseDate(startDate),
                                    endDate = parseDate(endDate),
                                    deadline = parseDate(deadline)
                                )
                                viewModel.createProject(
                                    req,
                                    onSuccess = {
                                        loading = false
                                        success = true
                                        onBack()
                                    },
                                    onError = {
                                        loading = false
                                        error = it
                                    }
                                )
                            },
                            enabled = !loading,
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            if (loading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text("Create", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun parseDate(input: String): String {
    // Accepts mm/dd/yyyy and returns yyyy-MM-dd'T'00:00:00.000Z
    return try {
        val parts = input.split("/")
        if (parts.size == 3) {
            val (mm, dd, yyyy) = parts
            val localDate = LocalDate.of(yyyy.toInt(), mm.toInt(), dd.toInt())
            localDate.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'00:00:00.000'Z'"))
        } else input
    } catch (e: Exception) {
        input
    }
} 