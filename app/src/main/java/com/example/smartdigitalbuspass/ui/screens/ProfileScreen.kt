package com.example.smartdigitalbuspass.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdigitalbuspass.data.DatabaseHelper
import com.example.smartdigitalbuspass.data.SessionManager
import com.example.smartdigitalbuspass.data.models.UserRole
import com.example.smartdigitalbuspass.theme.RoyalBlue
import com.example.smartdigitalbuspass.theme.TextSecondary
import com.example.smartdigitalbuspass.ui.components.AppBottomBar
import com.example.smartdigitalbuspass.ui.components.AppIcons
import com.example.smartdigitalbuspass.ui.navigation.Screen
import com.example.smartdigitalbuspass.utils.PasswordHasher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    dbHelper: DatabaseHelper,
    sessionManager: SessionManager,
    onNavigate: (Screen) -> Unit
) {
    val context = LocalContext.current
    val userId = sessionManager.getUserId()
    var user by remember(userId) { mutableStateOf(dbHelper.getUserById(userId)) }

    var isEditMode by remember { mutableStateOf(false) }
    var editName by remember(user) { mutableStateOf(user?.name ?: "") }
    var editPhone by remember(user) { mutableStateOf(user?.phone ?: "") }
    var editCollege by remember(user) { mutableStateOf(user?.collegeName ?: "") }
    var editStudentId by remember(user) { mutableStateOf(user?.studentId ?: "") }

    // Change Password states
    var showPasswordDialog by remember { mutableStateOf(false) }
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile", fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RoyalBlue)
            )
        },
        bottomBar = {
            AppBottomBar(currentScreen = Screen.Profile, onNavigate = onNavigate)
        }
    ) { paddingValues ->
        if (user == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("User not found")
            }
        } else {
            val u = user!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F7FA))
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Avatar Header Card
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
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(RoyalBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (u.role == UserRole.STUDENT) AppIcons.School else AppIcons.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = u.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF0F172A)
                        )

                        Text(
                            text = u.email,
                            fontSize = 13.sp,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            color = if (u.role == UserRole.STUDENT) Color(0xFFEFF6FF) else Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (u.role == UserRole.STUDENT) "🎓 Student Member" else "👤 Passenger Member",
                                color = if (u.role == UserRole.STUDENT) RoyalBlue else Color(0xFF475569),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Profile Information Card
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
                            Text("Account Information", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = RoyalBlue)
                            IconButton(onClick = { isEditMode = !isEditMode }) {
                                Icon(
                                    imageVector = if (isEditMode) AppIcons.Close else AppIcons.Edit,
                                    contentDescription = "Edit Profile",
                                    tint = RoyalBlue
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF1F5F9))

                        if (isEditMode) {
                            OutlinedTextField(
                                value = editName,
                                onValueChange = { editName = it },
                                label = { Text("Name") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = editPhone,
                                onValueChange = { editPhone = it },
                                label = { Text("Mobile Number") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            if (u.role == UserRole.STUDENT) {
                                OutlinedTextField(
                                    value = editCollege,
                                    onValueChange = { editCollege = it },
                                    label = { Text("College Name") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = editStudentId,
                                    onValueChange = { editStudentId = it },
                                    label = { Text("Student ID / PRN") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            Button(
                                onClick = {
                                    val updatedUser = u.copy(
                                        name = editName.trim(),
                                        phone = editPhone.trim(),
                                        collegeName = editCollege.trim(),
                                        studentId = editStudentId.trim()
                                    )
                                    val ok = dbHelper.updateUserProfile(updatedUser)
                                    if (ok) {
                                        user = updatedUser
                                        sessionManager.updateUserName(updatedUser.name)
                                        isEditMode = false
                                        Toast.makeText(context, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue)
                            ) {
                                Text("SAVE CHANGES", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            ProfileInfoRow(label = "Full Name", value = u.name)
                            ProfileInfoRow(label = "Email Address", value = u.email)
                            ProfileInfoRow(label = "Mobile Number", value = u.phone)
                            if (u.role == UserRole.STUDENT) {
                                ProfileInfoRow(label = "College / Institute", value = u.collegeName ?: "-")
                                ProfileInfoRow(label = "Student PRN / ID", value = u.studentId ?: "-")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Actions Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Account Security & Actions", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = RoyalBlue)
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = { showPasswordDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(AppIcons.Lock, contentDescription = null, tint = RoyalBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("CHANGE PASSWORD", color = RoyalBlue, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                sessionManager.clearSession()
                                Toast.makeText(context, "Logged Out Successfully", Toast.LENGTH_SHORT).show()
                                onNavigate(Screen.LoginRegister)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(AppIcons.Close, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("LOGOUT", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Change Password Dialog
        if (showPasswordDialog) {
            AlertDialog(
                onDismissRequest = { showPasswordDialog = false },
                title = { Text("Change Password", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = oldPassword,
                            onValueChange = { oldPassword = it },
                            label = { Text("Current Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("New Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (oldPassword.isBlank() || newPassword.isBlank()) {
                                Toast.makeText(context, "Please enter both passwords", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val oldHash = PasswordHasher.hashPassword(oldPassword)
                            if (user?.passwordHash != oldHash) {
                                Toast.makeText(context, "Incorrect current password", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val newHash = PasswordHasher.hashPassword(newPassword)
                            val ok = dbHelper.updateUserPassword(userId, newHash)
                            if (ok) {
                                user = user?.copy(passwordHash = newHash)
                                showPasswordDialog = false
                                oldPassword = ""
                                newPassword = ""
                                Toast.makeText(context, "Password Changed Successfully!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue)
                    ) {
                        Text("UPDATE")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPasswordDialog = false }) {
                        Text("CANCEL")
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(text = label, fontSize = 12.sp, color = TextSecondary)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
    }
}
