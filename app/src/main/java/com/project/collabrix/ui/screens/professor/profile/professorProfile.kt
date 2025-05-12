import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.draw.clip

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar()
        Spacer(modifier = Modifier.height(16.dp))
        ProfileHeader()
        Spacer(modifier = Modifier.height(16.dp))
        DangerZone()
        Spacer(modifier = Modifier.height(16.dp))
        AboutMeSection()
        Spacer(modifier = Modifier.height(16.dp))
        ResearchInterests()
    }
}

@Composable
fun TopAppBar() {
    Text("Profile Setting", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Text("Manage your personal information and account", fontSize = 14.sp, color = Color.Gray)
    Spacer(modifier = Modifier.height(8.dp))
    Button(onClick = { /* Edit action */ }) {
        Text("Edit Profile")
    }
}

@Composable
fun ProfileHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .padding(16.dp),
            tint = Color.DarkGray
        )
        Text("Dr Smith", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("Professor of Computer Science", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text("smith@university.edu", fontSize = 14.sp)
        Text("University of Technology", fontSize = 14.sp)
        Text("Department of CS", fontSize = 14.sp)
    }
}

@Composable
fun DangerZone() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFFEBEE))
            .padding(16.dp)
    ) {
        Text("Danger Zone", color = Color.Red, fontWeight = FontWeight.Bold)
        Text("Actions here cannot be undone", color = Color.Red.copy(alpha = 0.7f), fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { /* Delete account */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete Account", color = Color.White)
        }
    }
}

@Composable
fun AboutMeSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Text("About Me", fontWeight = FontWeight.Bold)
        Text(
            "Professor of computer science with over 15 years of experience in machine learning research.",
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
}

@Composable
fun ResearchInterests() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Text("Research Interests", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("AI", "ML", "LLM").forEach {
                AssistChip(label = it)
            }
        }
    }
}

@Composable
fun AssistChip(label: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.LightGray,
        modifier = Modifier.padding(end = 4.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            fontSize = 12.sp
        )
    }
}

@Preview
@Composable
fun PreviewProfileScreen() {
    ProfileScreen()
}
