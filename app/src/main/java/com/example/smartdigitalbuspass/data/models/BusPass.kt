package com.example.smartdigitalbuspass.data.models

enum class PassStatus {
    ACTIVE,
    EXPIRED,
    PENDING
}

enum class PaymentStatus {
    PAID,
    UNPAID
}

data class BusPass(
    val id: String, // e.g. "PASS-2026-89421"
    val userId: Long,
    val routeId: Long,
    val startDate: String, // YYYY-MM-DD
    val endDate: String,   // YYYY-MM-DD
    val status: PassStatus,
    val paymentStatus: PaymentStatus,
    val createdAt: String,
    val durationMonths: Int = 1,
    val pricePaid: Double = 0.0
)
