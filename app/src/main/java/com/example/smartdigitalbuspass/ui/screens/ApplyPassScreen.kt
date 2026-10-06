package com.example.smartdigitalbuspass.ui.screens

import android.widget.Toast
import androidx.compose.animation.Crossfade
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdigitalbuspass.data.DatabaseHelper
import com.example.smartdigitalbuspass.data.SessionManager
import com.example.smartdigitalbuspass.data.models.*
import com.example.smartdigitalbuspass.theme.RoyalBlue
import com.example.smartdigitalbuspass.theme.StatusActiveGreen
import com.example.smartdigitalbuspass.theme.TextSecondary
import com.example.smartdigitalbuspass.ui.components.AppIcons
import com.example.smartdigitalbuspass.ui.navigation.Screen
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyPassScreen(
    initialRouteId: Long?,
    dbHelper: DatabaseHelper,
    sessionManager: SessionManager,
    onNavigate: (Screen) -> Unit
) {
    val context = LocalContext.current
    val userId = sessionManager.getUserId()
    val currentUser = remember(userId) { dbHelper.getUserById(userId) }
    val allRoutes = remember { dbHelper.getAllRoutes() }

    var currentStep by remember { mutableStateOf(1) } // Step 1: Route, Step 2: Details, Step 3: Payment
    var selectedRoute by remember {
        mutableStateOf<BusRoute?>(
            allRoutes.find { it.id == initialRouteId } ?: allRoutes.firstOrNull()
        )
    }

    var selectedDurationMonths by remember { mutableStateOf(1) } // 1 Month or 3 Months
    var paymentMethod by remember { mutableStateOf("UPI") } // "UPI" or "Cash"
    var isProcessingPayment by remember { mutableStateOf(false) }

    // Start Date & End Date calculation
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val startDateStr = remember { dateFormat.format(Date()) }
    val endDateStr = remember(selectedDurationMonths) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, selectedDurationMonths)
        dateFormat.format(cal.time)
    }

    // Calculated price
    val isStudent = currentUser?.role == UserRole.STUDENT
    val baseMonthlyPrice = selectedRoute?.let {
        if (isStudent) it.studentPriceMonthly else it.priceMonthly
    } ?: 0.0
    val totalAmount = baseMonthlyPrice * selectedDurationMonths

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Apply For Bus Pass", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep > 1) {
                            currentStep--
                        } else {
                            onNavigate(Screen.Home)
                        }
                    }) {
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
        ) {
            // Step Progress Header Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StepItem(stepNumber = 1, title = "Select Route", isActive = currentStep >= 1, isCurrent = currentStep == 1)
                    HorizontalDivider(modifier = Modifier.width(20.dp), color = Color.LightGray)
                    StepItem(stepNumber = 2, title = "Details", isActive = currentStep >= 2, isCurrent = currentStep == 2)
                    HorizontalDivider(modifier = Modifier.width(20.dp), color = Color.LightGray)
                    StepItem(stepNumber = 3, title = "Payment", isActive = currentStep >= 3, isCurrent = currentStep == 3)
                }
            }

            Crossfade(
                targetState = currentStep,
                modifier = Modifier.weight(1f)
            ) { step ->
                when (step) {
                    1 -> Step1SelectRoute(
                        routes = allRoutes,
                        selectedRoute = selectedRoute,
                        isStudent = isStudent,
                        onSelectRoute = { selectedRoute = it },
                        onNext = {
                            if (selectedRoute != null) {
                                currentStep = 2
                            } else {
                                Toast.makeText(context, "Please select a route", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                    2 -> Step2PassDetails(
                        user = currentUser,
                        route = selectedRoute,
                        durationMonths = selectedDurationMonths,
                        startDate = startDateStr,
                        endDate = endDateStr,
                        totalAmount = totalAmount,
                        isStudent = isStudent,
                        onDurationChange = { selectedDurationMonths = it },
                        onNext = { currentStep = 3 }
                    )
                    3 -> Step3PaymentSummary(
                        user = currentUser,
                        route = selectedRoute,
                        startDate = startDateStr,
                        endDate = endDateStr,
                        durationMonths = selectedDurationMonths,
                        totalAmount = totalAmount,
                        paymentMethod = paymentMethod,
                        isProcessing = isProcessingPayment,
                        onPaymentMethodChange = { paymentMethod = it },
                        onConfirmPayment = {
                            if (currentUser == null || selectedRoute == null) return@Step3PaymentSummary
                            isProcessingPayment = true

                            // Generate Pass ID
                            val newPassId = "PASS-" + SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date()) + "-" + (1000..9999).random()

                            val newPass = BusPass(
                                id = newPassId,
                                userId = currentUser.id,
                                routeId = selectedRoute!!.id,
                                startDate = startDateStr,
                                endDate = endDateStr,
                                status = PassStatus.ACTIVE,
                                paymentStatus = PaymentStatus.PAID,
                                createdAt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
                                durationMonths = selectedDurationMonths,
                                pricePaid = totalAmount
                            )

                            val success = dbHelper.createPass(newPass)
                            isProcessingPayment = false

                            if (success) {
                                Toast.makeText(context, "Payment Successful! Pass Issued.", Toast.LENGTH_LONG).show()
                                onNavigate(Screen.DigitalPass(newPassId))
                            } else {
                                Toast.makeText(context, "Failed to create pass. Try again.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StepItem(
    stepNumber: Int,
    title: String,
    isActive: Boolean,
    isCurrent: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isActive) RoyalBlue else Color(0xFFE2E8F0)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber.toString(),
                color = if (isActive) Color.White else Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
            color = if (isCurrent) RoyalBlue else TextSecondary
        )
    }
}

@Composable
fun Step1SelectRoute(
    routes: List<BusRoute>,
    selectedRoute: BusRoute?,
    isStudent: Boolean,
    onSelectRoute: (BusRoute) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Select Bus Route",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
        Text(
            text = if (isStudent) "Student 50% discount pre-applied on all prices." else "Standard passenger monthly rates.",
            fontSize = 13.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            routes.forEach { route ->
                val isSelected = selectedRoute?.id == route.id
                val displayPrice = if (isStudent) route.studentPriceMonthly else route.priceMonthly

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectRoute(route) },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, RoyalBlue) else null,
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onSelectRoute(route) },
                            colors = RadioButtonDefaults.colors(selectedColor = RoyalBlue)
                        )

                        Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                            Text(
                                text = "${route.fromPlace} ➔ ${route.toPlace}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Stops: ${route.stops}",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = "Duration: ${route.duration}",
                                fontSize = 12.sp,
                                color = RoyalBlue,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "₹${displayPrice.toInt()}/mo",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = RoyalBlue
                            )
                            if (isStudent) {
                                Text(
                                    text = "₹${route.priceMonthly.toInt()}",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    style = androidx.compose.ui.text.TextStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                                )
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("CONTINUE TO DETAILS ➔", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}

@Composable
fun Step2PassDetails(
    user: User?,
    route: BusRoute?,
    durationMonths: Int,
    startDate: String,
    endDate: String,
    totalAmount: Double,
    isStudent: Boolean,
    onDurationChange: (Int) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Pass Details & Duration",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
        Text(
            text = "Verify applicant details and choose pass duration.",
            fontSize = 13.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // User Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Applicant Details", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = RoyalBlue)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF1F5F9))

                DetailRow(label = "Name", value = user?.name ?: "")
                DetailRow(label = "Category", value = if (isStudent) "Student (50% Concession)" else "General Passenger")
                if (isStudent) {
                    DetailRow(label = "College", value = user?.collegeName ?: "-")
                    DetailRow(label = "Student ID", value = user?.studentId ?: "-")
                }
                DetailRow(label = "Mobile", value = user?.phone ?: "-")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Duration Selection Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Select Duration", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = RoyalBlue)
                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FilterChip(
                        selected = durationMonths == 1,
                        onClick = { onDurationChange(1) },
                        label = { Text("1 Month Pass", fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = durationMonths == 3,
                        onClick = { onDurationChange(3) },
                        label = { Text("3 Months Pass", fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                DetailRow(label = "Start Date", value = startDate)
                DetailRow(label = "End Date", value = endDate)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Total Amount Summary Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total Payable", fontSize = 13.sp, color = TextSecondary)
                    Text("₹${totalAmount.toInt()}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = RoyalBlue)
                }
                if (isStudent) {
                    Surface(color = StatusActiveGreen.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp)) {
                        Text(
                            text = "50% Discounted",
                            color = StatusActiveGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("PROCEED TO PAYMENT ➔", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun Step3PaymentSummary(
    user: User?,
    route: BusRoute?,
    startDate: String,
    endDate: String,
    durationMonths: Int,
    totalAmount: Double,
    paymentMethod: String,
    isProcessing: Boolean,
    onPaymentMethodChange: (String) -> Unit,
    onConfirmPayment: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Payment & Confirmation",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
        Text(
            text = "Select mock payment mode to issue your digital pass instantly.",
            fontSize = 13.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Order Summary
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Pass Order Summary", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = RoyalBlue)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF1F5F9))

                DetailRow(label = "Route", value = "${route?.fromPlace} ➔ ${route?.toPlace}")
                DetailRow(label = "Passenger", value = user?.name ?: "")
                DetailRow(label = "Validity", value = "$startDate to $endDate ($durationMonths Mo)")
                DetailRow(label = "Amount Paid", value = "₹${totalAmount.toInt()}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Payment Method Selection
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Select Payment Option", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = RoyalBlue)
                Spacer(modifier = Modifier.height(8.dp))

                PaymentOptionRow(
                    title = "UPI / Google Pay / PhonePe (Mock)",
                    subtitle = "Instant digital verification",
                    isSelected = paymentMethod == "UPI",
                    onClick = { onPaymentMethodChange("UPI") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                PaymentOptionRow(
                    title = "Cash at Bus Depot (Mock)",
                    subtitle = "Pay directly at depot counter",
                    isSelected = paymentMethod == "Cash",
                    onClick = { onPaymentMethodChange("Cash") }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onConfirmPayment,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = StatusActiveGreen),
            shape = RoundedCornerShape(12.dp),
            enabled = !isProcessing
        ) {
            if (isProcessing) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Icon(AppIcons.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("PAY ₹${totalAmount.toInt()} & ISSUE PASS", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = TextSecondary)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
    }
}

@Composable
fun PaymentOptionRow(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFFEFF6FF) else Color(0xFFF8FAFC)),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, RoyalBlue) else null,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = isSelected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = RoyalBlue))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))
                Text(text = subtitle, fontSize = 12.sp, color = TextSecondary)
            }
        }
    }
}
