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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdigitalbuspass.theme.RoyalBlue
import com.example.smartdigitalbuspass.theme.TextSecondary
import com.example.smartdigitalbuspass.ui.components.AppIcons
import com.example.smartdigitalbuspass.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutHelpScreen(
    onNavigate: (Screen) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About & Help", fontWeight = FontWeight.Bold, color = Color.White) },
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
            // App Banner Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = AppIcons.Bus,
                        contentDescription = null,
                        tint = RoyalBlue,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Smart Digital Bus Pass",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "Version 1.0.0 • College Final Project",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "A 100% offline-ready digital bus ticketing and pass management system built for students and city passengers with custom Canvas QR codes and SQLite database.",
                        fontSize = 13.sp,
                        color = Color(0xFF334155),
                        lineHeight = 18.sp
                    )
                }
            }

            // FAQ Card 1
            FaqItemCard(
                question = "How does the student discount work?",
                answer = "During account registration, choose the 'Student' role and enter your college name and PRN. Students automatically receive a 50% discount on all bus routes."
            )

            // FAQ Card 2
            FaqItemCard(
                question = "How do I show my pass to the conductor?",
                answer = "Open 'Active Pass' on the Home screen or tap 'My Pass' to display your full digital ticket with the QR code. You can also tap 'Save Pass' to keep an image copy in your phone's Gallery."
            )

            // FAQ Card 3
            FaqItemCard(
                question = "What happens when my pass expires?",
                answer = "The app automatically updates pass status to 'EXPIRED' when the validity end date passes. You can re-apply for a new pass anytime from the 'Apply New Pass' screen."
            )

            // FAQ Card 4
            FaqItemCard(
                question = "Is internet or server required?",
                answer = "No! The app runs 100% locally on your device using built-in SQLite database and SharedPreferences with zero external API dependencies."
            )
        }
    }
}

@Composable
fun FaqItemCard(question: String, answer: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(AppIcons.Info, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = question, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0F172A))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = answer, fontSize = 13.sp, color = TextSecondary, lineHeight = 18.sp)
        }
    }
}
