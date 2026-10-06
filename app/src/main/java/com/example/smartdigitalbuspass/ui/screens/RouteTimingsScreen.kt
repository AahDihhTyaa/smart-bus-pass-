package com.example.smartdigitalbuspass.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdigitalbuspass.data.DatabaseHelper
import com.example.smartdigitalbuspass.data.SessionManager
import com.example.smartdigitalbuspass.data.models.BusRoute
import com.example.smartdigitalbuspass.data.models.UserRole
import com.example.smartdigitalbuspass.theme.RoyalBlue
import com.example.smartdigitalbuspass.theme.TextSecondary
import com.example.smartdigitalbuspass.ui.components.AppBottomBar
import com.example.smartdigitalbuspass.ui.components.AppIcons
import com.example.smartdigitalbuspass.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteTimingsScreen(
    dbHelper: DatabaseHelper,
    sessionManager: SessionManager,
    onNavigate: (Screen) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val userRole = sessionManager.getUserRole()
    val isStudent = userRole == UserRole.STUDENT

    val routes by remember(searchQuery) {
        mutableStateOf(dbHelper.searchRoutes(searchQuery))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Routes & Bus Timings", fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RoyalBlue)
            )
        },
        bottomBar = {
            AppBottomBar(currentScreen = Screen.RouteTimings, onNavigate = onNavigate)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Search Input Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by place or stop name...") },
                leadingIcon = { Icon(AppIcons.Search, contentDescription = null, tint = RoyalBlue) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(AppIcons.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (routes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = AppIcons.Bus,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No matching bus routes found", color = TextSecondary)
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(routes) { route ->
                        RouteCard(
                            route = route,
                            isStudent = isStudent,
                            onApply = { onNavigate(Screen.ApplyPass(route.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RouteCard(
    route: BusRoute,
    isStudent: Boolean,
    onApply: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${route.fromPlace} ➔ ${route.toPlace}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF0F172A)
                    )
                }

                Surface(
                    color = Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = route.duration,
                        color = RoyalBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = AppIcons.Place,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp).padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Stops: ${route.stops}",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = AppIcons.DateRange,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Timings: ${route.timings}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF334155)
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Monthly Fare", fontSize = 11.sp, color = TextSecondary)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "₹${(if (isStudent) route.studentPriceMonthly else route.priceMonthly).toInt()}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = RoyalBlue
                        )
                        if (isStudent) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "₹${route.priceMonthly.toInt()}",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                style = androidx.compose.ui.text.TextStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "(Student 50% Off)",
                                fontSize = 11.sp,
                                color = Color(0xFF10B981),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Button(
                    onClick = onApply,
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("APPLY PASS", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
