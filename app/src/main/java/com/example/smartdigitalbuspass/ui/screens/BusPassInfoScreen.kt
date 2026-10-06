package com.example.smartdigitalbuspass.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdigitalbuspass.theme.RoyalBlue
import com.example.smartdigitalbuspass.ui.components.AppIcons
import com.example.smartdigitalbuspass.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusPassInfoScreen(
    onNavigate: (Screen) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bus Pass Info & Rules", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { onNavigate(Screen.Home) }) {
                        Icon(AppIcons.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RoyalBlue)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Student Discount Banner Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = AppIcons.School,
                        contentDescription = null,
                        tint = RoyalBlue,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "🎓 50% Concession for Students!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = RoyalBlue
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "College students automatically get 50% discount on all monthly routes with valid Student ID.",
                            fontSize = 13.sp,
                            color = Color(0xFF1E293B)
                        )
                    }
                }
            }

            // Info Section 1: Pass Types
            InfoCard(
                title = "Pass Types & Options",
                icon = AppIcons.Bus,
                items = listOf(
                    "1-Month Pass: Standard 30-day unlimited journey pass on chosen route.",
                    "3-Month Pass: Quarterly pass for long-term commuters with bonus savings.",
                    "Student Pass: Specially subsidized pass for verified college students.",
                    "General Passenger Pass: Standard monthly pass for daily commuters."
                )
            )

            // Info Section 2: Documents Required
            InfoCard(
                title = "Required Verification Documents",
                icon = AppIcons.List,
                items = listOf(
                    "For Students: Valid College ID Card or Bonafide Certificate with PRN.",
                    "For Normal Passengers: Government Photo ID (Aadhaar / Voter ID / Driving License).",
                    "Digital verification is completed instantly during online registration."
                )
            )

            // Info Section 3: Rules & Guidelines
            InfoCard(
                title = "Pass Usage Rules",
                icon = AppIcons.Info,
                items = listOf(
                    "Bus passes are non-transferable and valid only for the registered person.",
                    "Present the digital QR code on your phone when requested by the conductor.",
                    "Passes automatically expire at 11:59 PM on the end date.",
                    "In case of lost phone, log in on another device to access your active pass."
                )
            )

            Button(
                onClick = { onNavigate(Screen.ApplyPass()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(AppIcons.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("APPLY FOR PASS NOW", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    icon: ImageVector,
    items: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF0F172A))
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))
            items.forEach { item ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(text = "• ", fontWeight = FontWeight.Bold, color = RoyalBlue)
                    Text(text = item, fontSize = 13.sp, color = Color(0xFF334155), lineHeight = 18.sp)
                }
            }
        }
    }
}
