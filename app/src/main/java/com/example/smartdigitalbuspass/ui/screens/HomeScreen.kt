package com.example.smartdigitalbuspass.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdigitalbuspass.data.DatabaseHelper
import com.example.smartdigitalbuspass.data.SessionManager
import com.example.smartdigitalbuspass.data.models.UserRole
import com.example.smartdigitalbuspass.theme.AccentGold
import com.example.smartdigitalbuspass.theme.RoyalBlue
import com.example.smartdigitalbuspass.theme.StatusActiveGreen
import com.example.smartdigitalbuspass.theme.TextSecondary
import com.example.smartdigitalbuspass.ui.components.AppBottomBar
import com.example.smartdigitalbuspass.ui.components.AppIcons
import com.example.smartdigitalbuspass.ui.navigation.Screen
import com.example.smartdigitalbuspass.utils.DemoQrCodeView

@Composable
fun HomeScreen(
    dbHelper: DatabaseHelper,
    sessionManager: SessionManager,
    onNavigate: (Screen) -> Unit
) {
    val userId = sessionManager.getUserId()
    val userName = sessionManager.getUserName()
    val userRole = sessionManager.getUserRole()

    val activePass = remember(userId) { dbHelper.getActivePassForUser(userId) }
    val activeRoute = remember(activePass) {
        activePass?.let { dbHelper.getRouteById(it.routeId) }
    }

    Scaffold(
        bottomBar = {
            AppBottomBar(currentScreen = Screen.Home, onNavigate = onNavigate)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = RoyalBlue,
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Hello, $userName! 👋",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (userRole == UserRole.STUDENT) "🎓 Student Pass Holder (50% Off)" else "🚌 Normal Passenger",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        IconButton(
                            onClick = { onNavigate(Screen.AboutHelp) }
                        ) {
                            Icon(
                                imageVector = AppIcons.Info,
                                contentDescription = "Help & Info",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Active Pass Card Section
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Active Pass",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (activePass != null && activeRoute != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigate(Screen.DigitalPass(activePass.id)) },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
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
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = activePass.id,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = RoyalBlue
                                    )
                                }

                                Surface(
                                    color = StatusActiveGreen.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "VALID",
                                        color = StatusActiveGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE2E8F0))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${activeRoute.fromPlace} ➔ ${activeRoute.toPlace}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color(0xFF0F172A)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Valid Till: ${activePass.endDate}",
                                        fontSize = 13.sp,
                                        color = TextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Tap to view full Digital QR Pass",
                                        fontSize = 12.sp,
                                        color = RoyalBlue,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                DemoQrCodeView(
                                    passId = activePass.id,
                                    sizeDp = 80.dp
                                )
                            }
                        }
                    }
                } else {
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
                                imageVector = AppIcons.Star,
                                contentDescription = null,
                                tint = AccentGold,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No Active Bus Pass Found",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                text = "Apply now to get your instant digital pass.",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { onNavigate(Screen.ApplyPass()) },
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(AppIcons.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("APPLY NEW PASS")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Services Grid
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Quick Services",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionCard(
                        title = "Apply New Pass",
                        subtitle = "Instant approval",
                        icon = AppIcons.Add,
                        iconBg = Color(0xFFEEF2FF),
                        iconTint = RoyalBlue,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.ApplyPass()) }
                    )

                    QuickActionCard(
                        title = "My Pass",
                        subtitle = activePass?.let { "Active Pass" } ?: "No Active Pass",
                        icon = AppIcons.CheckCircle,
                        iconBg = Color(0xFFECFDF5),
                        iconTint = StatusActiveGreen,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (activePass != null) {
                                onNavigate(Screen.DigitalPass(activePass.id))
                            } else {
                                onNavigate(Screen.ApplyPass())
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionCard(
                        title = "Route & Timings",
                        subtitle = "Search bus stops",
                        icon = AppIcons.Bus,
                        iconBg = Color(0xFFFFF7ED),
                        iconTint = AccentGold,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.RouteTimings) }
                    )

                    QuickActionCard(
                        title = "Pass History",
                        subtitle = "Past transactions",
                        icon = AppIcons.History,
                        iconBg = Color(0xFFF3E8FF),
                        iconTint = Color(0xFF9333EA),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.PassHistory) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionCard(
                        title = "Profile",
                        subtitle = "Manage account",
                        icon = AppIcons.Person,
                        iconBg = Color(0xFFEFF6FF),
                        iconTint = Color(0xFF2563EB),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Profile) }
                    )

                    QuickActionCard(
                        title = "Bus Pass Info",
                        subtitle = "Rules & Discounts",
                        icon = AppIcons.Info,
                        iconBg = Color(0xFFFEF2F2),
                        iconTint = Color(0xFFEF4444),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.BusPassInfo) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}
