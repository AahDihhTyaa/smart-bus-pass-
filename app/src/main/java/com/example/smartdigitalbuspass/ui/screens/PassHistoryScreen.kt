package com.example.smartdigitalbuspass.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdigitalbuspass.data.DatabaseHelper
import com.example.smartdigitalbuspass.data.SessionManager
import com.example.smartdigitalbuspass.data.models.BusPass
import com.example.smartdigitalbuspass.data.models.BusRoute
import com.example.smartdigitalbuspass.data.models.PassStatus
import com.example.smartdigitalbuspass.theme.RoyalBlue
import com.example.smartdigitalbuspass.theme.StatusActiveGreen
import com.example.smartdigitalbuspass.theme.StatusExpiredRed
import com.example.smartdigitalbuspass.theme.TextSecondary
import com.example.smartdigitalbuspass.ui.components.AppBottomBar
import com.example.smartdigitalbuspass.ui.components.AppIcons
import com.example.smartdigitalbuspass.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassHistoryScreen(
    dbHelper: DatabaseHelper,
    sessionManager: SessionManager,
    onNavigate: (Screen) -> Unit
) {
    val userId = sessionManager.getUserId()
    val passes by remember(userId) {
        mutableStateOf(dbHelper.getUserPasses(userId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pass History", fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RoyalBlue)
            )
        },
        bottomBar = {
            AppBottomBar(currentScreen = Screen.PassHistory, onNavigate = onNavigate)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (passes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = AppIcons.History,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No Pass History Found",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = "You haven't applied for any bus pass yet.",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onNavigate(Screen.ApplyPass()) },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(AppIcons.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("APPLY FIRST PASS")
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(passes) { pass ->
                        val route = remember(pass.routeId) { dbHelper.getRouteById(pass.routeId) }
                        PassHistoryCard(
                            pass = pass,
                            route = route,
                            onClick = { onNavigate(Screen.DigitalPass(pass.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PassHistoryCard(
    pass: BusPass,
    route: BusRoute?,
    onClick: () -> Unit
) {
    val isActive = pass.status == PassStatus.ACTIVE

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = AppIcons.Bus,
                        contentDescription = null,
                        tint = RoyalBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = pass.id,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = RoyalBlue
                    )
                }

                Surface(
                    color = if (isActive) StatusActiveGreen.copy(alpha = 0.15f) else StatusExpiredRed.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (isActive) StatusActiveGreen else StatusExpiredRed)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isActive) "ACTIVE" else "EXPIRED",
                            color = if (isActive) StatusActiveGreen else StatusExpiredRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${route?.fromPlace ?: "Unknown"} ➔ ${route?.toPlace ?: "Unknown"}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Validity: ${pass.startDate} to ${pass.endDate}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Text(
                    text = "Paid: ₹${pass.pricePaid.toInt()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF334155)
                )
            }
        }
    }
}
