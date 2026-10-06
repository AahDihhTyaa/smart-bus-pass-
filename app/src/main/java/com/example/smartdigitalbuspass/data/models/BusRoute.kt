package com.example.smartdigitalbuspass.data.models

data class BusRoute(
    val id: Long = 0,
    val fromPlace: String,
    val toPlace: String,
    val stops: String,
    val priceMonthly: Double,
    val studentPriceMonthly: Double,
    val duration: String,
    val timings: String
) {
    val routeTitle: String get() = "$fromPlace to $toPlace"
}
