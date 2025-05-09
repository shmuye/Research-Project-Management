package andorid.example.collabrix.View.StudentUi.Pages


import andorid.example.collabrix.View.StudentUi.Components.MyProjects
import andorid.example.collabrix.ViewModel.DashboardState
import andorid.example.collabrix.ViewModel.DashboardViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.project.collabrix.R
import com.project.collabrix.ui.screens.Student.Components.SideBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardPage(
    navController: NavHostController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    // for the Side bar navigation
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Collect state from ViewModel
    val state by viewModel.state.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                SideBar(
                    scope = scope,
                    drawerState = drawerState,
                    onMenuItemClick = { route ->
                        navController.navigate(route)
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Collabrix",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = "logo",
                            modifier = Modifier.size(50.dp)
                        )
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu Icon",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                        )
                    },
                    modifier = Modifier.shadow(
                        elevation = 8.dp,
                        ambientColor = Color.Black,
                        spotColor = Color.Black
                    )
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 30.dp)
                    .fillMaxSize()
            ) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(vertical = 10.dp)
                    ) {
                        Text(
                            text = "Student Dashboard",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(18.dp))

                    // Applied projects box
                    Card(
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Applied Projects",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = when (state) {
                                    is DashboardState.Success -> "${(state as DashboardState.Success).totalAppliedProjects.size}"
                                    else -> "0"
                                },
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    // Active Projects indicator Box
                    Card(
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Active Projects",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = when (state) {
                                    is DashboardState.Success -> "${(state as DashboardState.Success).activeProjects.size}"
                                    else -> "0"
                                },
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "Your Active Projects",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // List of Active projects
                    when (state) {
                        is DashboardState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is DashboardState.Error -> Text(
                            text = (state as DashboardState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                        is DashboardState.Success -> MyProjects(
                            projects = (state as DashboardState.Success).activeProjects,
                            isLoading = false,
                            error = null,
                            onProjectClick = { projectId ->
                                // Handle project click
                            }
                        )
                        else -> Text("No projects available")
                    }
                }
            }
        }
    }
}