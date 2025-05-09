package andorid.example.collabrix.View.StudentUi.Pages

import andorid.example.collabrix.View.StudentUi.Components.MySearchBar
import andorid.example.collabrix.View.StudentUi.Components.RecommendedResearches
import android.example.collabrix.ViewModel.BrowseResearchesState
import android.example.collabrix.ViewModel.BrowseResearchesViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.collabrix.R
import com.project.collabrix.ui.screens.Student.Components.SideBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseResearch(
    navController: NavHostController,
    viewModel: BrowseResearchesViewModel = viewModel()
) {
    // for the side bar navigation
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // for the search bar
    var searchQuery by remember { mutableStateOf("") }

    // Collect state from ViewModel
    val state by viewModel.state.collectAsState()

    // Handle search with debounce
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            viewModel.searchProjects(searchQuery)
        } else {
            viewModel.loadAllProjects()
        }
    }

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
        ) { innerpadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerpadding)
                    .padding(horizontal = 30.dp)
                    .fillMaxSize()
            ) {
                item {
                    Text(text = "Browse Research", fontSize = 32.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(10.dp))

                    MySearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it }
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    when (state) {
                        is BrowseResearchesState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is BrowseResearchesState.Error -> {
                            Text(
                                text = (state as BrowseResearchesState.Error).message,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        is BrowseResearchesState.Success -> {
                            val successState = state as BrowseResearchesState.Success
                            Column {
                                // Available Projects Section
                                Text(
                                    text = "Available Projects",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                                RecommendedResearches(
                                    projects = successState.availableProjects,
                                    isLoading = false,
                                    error = null,
                                    onApplyClick = { projectId ->
                                        viewModel.applyToProject(projectId)
                                    }
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                // Pending Projects Section
                                Text(
                                    text = "Pending Applications",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                                RecommendedResearches(
                                    projects = successState.pendingProjects,
                                    isLoading = false,
                                    error = null,
                                    onApplyClick = { projectId ->
                                        viewModel.withdrawApplication(projectId)
                                    }
                                )
                            }
                        }
                        else -> {
                            Text(
                                text = "No projects available",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}