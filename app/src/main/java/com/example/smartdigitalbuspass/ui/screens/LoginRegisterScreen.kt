package com.example.smartdigitalbuspass.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdigitalbuspass.data.DatabaseHelper
import com.example.smartdigitalbuspass.data.SessionManager
import com.example.smartdigitalbuspass.data.models.User
import com.example.smartdigitalbuspass.data.models.UserRole
import com.example.smartdigitalbuspass.theme.RoyalBlue
import com.example.smartdigitalbuspass.theme.TextSecondary
import com.example.smartdigitalbuspass.ui.components.AppIcons
import com.example.smartdigitalbuspass.ui.navigation.Screen
import com.example.smartdigitalbuspass.utils.PasswordHasher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRegisterScreen(
    dbHelper: DatabaseHelper,
    sessionManager: SessionManager,
    onNavigate: (Screen) -> Unit
) {
    val context = LocalContext.current

    var isRegisterMode by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }

    // Form inputs
    var name by remember { mutableStateOf("") }
    var emailOrPhone by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var collegeName by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    // Errors
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .verticalScroll(rememberScrollState())
    ) {
        // Top Header Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = RoyalBlue,
                    shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                )
                .padding(top = 40.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = AppIcons.Bus,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (isRegisterMode) "Create Account" else "Welcome Back",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isRegisterMode) "Sign up for your digital bus pass" else "Login to manage your bus pass",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Role Selection Tabs (Student | Passenger)
        TabRow(
            selectedTabIndex = if (selectedRole == UserRole.STUDENT) 0 else 1,
            containerColor = Color.White,
            contentColor = RoyalBlue,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .background(Color.White, shape = RoundedCornerShape(12.dp))
        ) {
            Tab(
                selected = selectedRole == UserRole.STUDENT,
                onClick = { selectedRole = UserRole.STUDENT },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppIcons.School,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Student", fontWeight = FontWeight.SemiBold)
                    }
                }
            )
            Tab(
                selected = selectedRole == UserRole.PASSENGER,
                onClick = { selectedRole = UserRole.PASSENGER },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppIcons.Person,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Passenger", fontWeight = FontWeight.SemiBold)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Form Container Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                if (errorMessage != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = Color(0xFFDC2626),
                            modifier = Modifier.padding(12.dp),
                            fontSize = 13.sp
                        )
                    }
                }

                if (isRegisterMode) {
                    // Name
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it; errorMessage = null },
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(AppIcons.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Email
                    OutlinedTextField(
                        value = emailOrPhone,
                        onValueChange = { emailOrPhone = it; errorMessage = null },
                        label = { Text("Email Address") },
                        leadingIcon = { Icon(AppIcons.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Phone Number
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it; errorMessage = null },
                        label = { Text("Mobile Number") },
                        leadingIcon = { Icon(AppIcons.Phone, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (selectedRole == UserRole.STUDENT) {
                        // College Name
                        OutlinedTextField(
                            value = collegeName,
                            onValueChange = { collegeName = it; errorMessage = null },
                            label = { Text("College / Institute Name") },
                            leadingIcon = { Icon(AppIcons.School, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Student ID / PRN
                        OutlinedTextField(
                            value = studentId,
                            onValueChange = { studentId = it; errorMessage = null },
                            label = { Text("Student ID / PRN No.") },
                            leadingIcon = { Icon(AppIcons.Star, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                } else {
                    // Login fields: Email or Phone
                    OutlinedTextField(
                        value = emailOrPhone,
                        onValueChange = { emailOrPhone = it; errorMessage = null },
                        label = { Text("Email or Mobile Number") },
                        leadingIcon = { Icon(AppIcons.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Password field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; errorMessage = null },
                    label = { Text("Password") },
                    leadingIcon = { Icon(AppIcons.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) AppIcons.Close else AppIcons.Info,
                                contentDescription = "Toggle Password"
                            )
                        }
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Submit Button
                Button(
                    onClick = {
                        errorMessage = null
                        if (emailOrPhone.isBlank() || password.isBlank()) {
                            errorMessage = "Please enter all required fields."
                            return@Button
                        }

                        val passHash = PasswordHasher.hashPassword(password)

                        if (isRegisterMode) {
                            if (name.isBlank() || phone.isBlank()) {
                                errorMessage = "Please enter your name and phone number."
                                return@Button
                            }
                            if (selectedRole == UserRole.STUDENT && (collegeName.isBlank() || studentId.isBlank())) {
                                errorMessage = "Students must provide College Name & Student ID for verification."
                                return@Button
                            }

                            isLoading = true
                            val newUser = User(
                                name = name.trim(),
                                email = emailOrPhone.trim(),
                                phone = phone.trim(),
                                role = selectedRole,
                                collegeName = if (selectedRole == UserRole.STUDENT) collegeName.trim() else null,
                                studentId = if (selectedRole == UserRole.STUDENT) studentId.trim() else null,
                                passwordHash = passHash
                            )

                            val userId = dbHelper.registerUser(newUser)
                            isLoading = false

                            if (userId == -1L) {
                                errorMessage = "An account with this email already exists."
                            } else {
                                sessionManager.saveSession(userId, newUser.name, newUser.role)
                                Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
                                onNavigate(Screen.Home)
                            }
                        } else {
                            // Login
                            isLoading = true
                            val user = dbHelper.loginUser(emailOrPhone, passHash)
                            isLoading = false

                            if (user == null) {
                                errorMessage = "Invalid email/phone or password."
                            } else {
                                sessionManager.saveSession(user.id, user.name, user.role)
                                Toast.makeText(context, "Welcome back, ${user.name}!", Toast.LENGTH_SHORT).show()
                                onNavigate(Screen.Home)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                    shape = RoundedCornerShape(10.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = if (isRegisterMode) "REGISTER NOW" else "LOGIN",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Toggle Login / Register mode
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isRegisterMode) "Already have an account? " else "Don't have an account? ",
                color = TextSecondary,
                fontSize = 14.sp
            )
            Text(
                text = if (isRegisterMode) "Login" else "Register",
                color = RoyalBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    isRegisterMode = !isRegisterMode
                    errorMessage = null
                }
            )
        }
    }
}
