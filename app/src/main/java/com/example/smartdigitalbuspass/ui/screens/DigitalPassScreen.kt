package com.example.smartdigitalbuspass.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdigitalbuspass.data.DatabaseHelper
import com.example.smartdigitalbuspass.data.SessionManager
import com.example.smartdigitalbuspass.data.models.PassStatus
import com.example.smartdigitalbuspass.data.models.UserRole
import com.example.smartdigitalbuspass.theme.RoyalBlue
import com.example.smartdigitalbuspass.theme.StatusActiveGreen
import com.example.smartdigitalbuspass.theme.StatusExpiredRed
import com.example.smartdigitalbuspass.theme.TextSecondary
import com.example.smartdigitalbuspass.ui.components.AppIcons
import com.example.smartdigitalbuspass.ui.navigation.Screen
import com.example.smartdigitalbuspass.utils.DemoQrCodeView
import com.example.smartdigitalbuspass.utils.PassExporter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigitalPassScreen(
    passId: String,
    dbHelper: DatabaseHelper,
    sessionManager: SessionManager,
    onNavigate: (Screen) -> Unit
) {
    val context = LocalContext.current
    val pass = remember(passId) { dbHelper.getPassById(passId) }
    val route = remember(pass) { pass?.let { dbHelper.getRouteById(it.routeId) } }
    val user = remember(pass) { pass?.let { dbHelper.getUserById(it.userId) } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Digital Bus Pass", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { onNavigate(Screen.Home) }) {
                        Icon(AppIcons.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RoyalBlue)
            )
        }
    ) { paddingValues ->
        if (pass == null || route == null || user == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Bus Pass Not Found", fontSize = 16.sp, color = TextSecondary)
            }
        } else {
            val isActive = pass.status == PassStatus.ACTIVE

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F7FA))
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Pass Main Ticket Container Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Header Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(RoyalBlue)
                                .padding(18.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = AppIcons.Bus,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "SMART DIGITAL BUS PASS",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        letterSpacing = 1.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = pass.id,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Status & Category Badges
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = if (user.role == UserRole.STUDENT) Color(0xFFEFF6FF) else Color(0xFFF1F5F9),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = if (user.role == UserRole.STUDENT) "🎓 STUDENT PASS" else "👤 PASSENGER PASS",
                                        color = if (user.role == UserRole.STUDENT) RoyalBlue else Color(0xFF475569),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }

                                Surface(
                                    color = if (isActive) StatusActiveGreen.copy(alpha = 0.15f) else StatusExpiredRed.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(if (isActive) StatusActiveGreen else StatusExpiredRed)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (isActive) "VALID PASS" else "EXPIRED",
                                            color = if (isActive) StatusActiveGreen else StatusExpiredRed,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Large Canvas QR Code
                            DemoQrCodeView(
                                passId = pass.id,
                                sizeDp = 200.dp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Scan QR code for conductor verification",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                textAlign = TextAlign.Center
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFE2E8F0))

                            // Pass details grid
                            PassDetailItem(label = "Passenger Name", value = user.name)
                            if (user.role == UserRole.STUDENT) {
                                PassDetailItem(label = "College / PRN", value = "${user.collegeName ?: "-"} (${user.studentId ?: "-"})")
                            }
                            PassDetailItem(label = "Route", value = "${route.fromPlace} ➔ ${route.toPlace}")
                            PassDetailItem(label = "Via Stops", value = route.stops)
                            PassDetailItem(label = "Valid From", value = pass.startDate)
                            PassDetailItem(label = "Valid Till", value = pass.endDate)
                            PassDetailItem(label = "Amount Paid", value = "₹${pass.pricePaid.toInt()} (${pass.paymentStatus.name})")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Download & Share Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            PassExporter.downloadPassImage(context, pass, route, user)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(AppIcons.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SAVE PASS", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            PassExporter.sharePassDetails(context, pass, route, user.name)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(AppIcons.Share, contentDescription = null, tint = RoyalBlue)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SHARE", fontWeight = FontWeight.Bold, color = RoyalBlue)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun PassDetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = TextSecondary)
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF0F172A),
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f, fill = false).padding(start = 16.dp)
        )
    }
}
