package com.example.smartdigitalbuspass.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.smartdigitalbuspass.data.models.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "smart_bus_pass.db"
        private const val DATABASE_VERSION = 1

        // Table Names
        private const val TABLE_USERS = "users"
        private const val TABLE_ROUTES = "routes"
        private const val TABLE_PASSES = "passes"

        // Users Columns
        private const val KEY_USER_ID = "id"
        private const val KEY_USER_NAME = "name"
        private const val KEY_USER_EMAIL = "email"
        private const val KEY_USER_PHONE = "phone"
        private const val KEY_USER_ROLE = "role"
        private const val KEY_USER_COLLEGE = "collegeName"
        private const val KEY_USER_STUDENT_ID = "studentId"
        private const val KEY_USER_PASSWORD_HASH = "passwordHash"

        // Routes Columns
        private const val KEY_ROUTE_ID = "id"
        private const val KEY_ROUTE_FROM = "fromPlace"
        private const val KEY_ROUTE_TO = "toPlace"
        private const val KEY_ROUTE_STOPS = "stops"
        private const val KEY_ROUTE_PRICE = "priceMonthly"
        private const val KEY_ROUTE_STUDENT_PRICE = "studentPriceMonthly"
        private const val KEY_ROUTE_DURATION = "duration"
        private const val KEY_ROUTE_TIMINGS = "timings"

        // Passes Columns
        private const val KEY_PASS_ID = "id"
        private const val KEY_PASS_USER_ID = "userId"
        private const val KEY_PASS_ROUTE_ID = "routeId"
        private const val KEY_PASS_START_DATE = "startDate"
        private const val KEY_PASS_END_DATE = "endDate"
        private const val KEY_PASS_STATUS = "status"
        private const val KEY_PASS_PAYMENT_STATUS = "paymentStatus"
        private const val KEY_PASS_CREATED_AT = "createdAt"
        private const val KEY_PASS_DURATION = "durationMonths"
        private const val KEY_PASS_PRICE_PAID = "pricePaid"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $KEY_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_USER_NAME TEXT NOT NULL,
                $KEY_USER_EMAIL TEXT UNIQUE NOT NULL,
                $KEY_USER_PHONE TEXT NOT NULL,
                $KEY_USER_ROLE TEXT NOT NULL,
                $KEY_USER_COLLEGE TEXT,
                $KEY_USER_STUDENT_ID TEXT,
                $KEY_USER_PASSWORD_HASH TEXT NOT NULL
            )
        """.trimIndent()

        val createRoutesTable = """
            CREATE TABLE $TABLE_ROUTES (
                $KEY_ROUTE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_ROUTE_FROM TEXT NOT NULL,
                $KEY_ROUTE_TO TEXT NOT NULL,
                $KEY_ROUTE_STOPS TEXT NOT NULL,
                $KEY_ROUTE_PRICE REAL NOT NULL,
                $KEY_ROUTE_STUDENT_PRICE REAL NOT NULL,
                $KEY_ROUTE_DURATION TEXT NOT NULL,
                $KEY_ROUTE_TIMINGS TEXT NOT NULL
            )
        """.trimIndent()

        val createPassesTable = """
            CREATE TABLE $TABLE_PASSES (
                $KEY_PASS_ID TEXT PRIMARY KEY,
                $KEY_PASS_USER_ID INTEGER NOT NULL,
                $KEY_PASS_ROUTE_ID INTEGER NOT NULL,
                $KEY_PASS_START_DATE TEXT NOT NULL,
                $KEY_PASS_END_DATE TEXT NOT NULL,
                $KEY_PASS_STATUS TEXT NOT NULL,
                $KEY_PASS_PAYMENT_STATUS TEXT NOT NULL,
                $KEY_PASS_CREATED_AT TEXT NOT NULL,
                $KEY_PASS_DURATION INTEGER DEFAULT 1,
                $KEY_PASS_PRICE_PAID REAL DEFAULT 0.0,
                FOREIGN KEY($KEY_PASS_USER_ID) REFERENCES $TABLE_USERS($KEY_USER_ID),
                FOREIGN KEY($KEY_PASS_ROUTE_ID) REFERENCES $TABLE_ROUTES($KEY_ROUTE_ID)
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createRoutesTable)
        db.execSQL(createPassesTable)

        // Seed default sample routes
        seedSampleRoutes(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PASSES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROUTES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    private fun seedSampleRoutes(db: SQLiteDatabase) {
        val sampleRoutes = listOf(
            BusRoute(
                fromPlace = "Khalapur",
                toPlace = "Mumbai",
                stops = "Khalapur, Chowk, Panvel, Vashi, Dadar, Mumbai CSMT",
                priceMonthly = 1200.0,
                studentPriceMonthly = 600.0,
                duration = "2h 15m",
                timings = "06:30 AM, 08:30 AM, 01:00 PM, 05:30 PM"
            ),
            BusRoute(
                fromPlace = "Khalapur",
                toPlace = "Panvel",
                stops = "Khalapur, Shedung, Chowk, Old Panvel, Panvel Station",
                priceMonthly = 600.0,
                studentPriceMonthly = 300.0,
                duration = "45 mins",
                timings = "07:00 AM, 09:00 AM, 12:00 PM, 04:00 PM, 07:00 PM"
            ),
            BusRoute(
                fromPlace = "Khalapur",
                toPlace = "Taloja",
                stops = "Khalapur, Mohopada, Reshim, Taloja MIDC",
                priceMonthly = 750.0,
                studentPriceMonthly = 375.0,
                duration = "55 mins",
                timings = "07:30 AM, 10:00 AM, 02:30 PM, 06:00 PM"
            ),
            BusRoute(
                fromPlace = "Khalapur",
                toPlace = "Navi Mumbai",
                stops = "Khalapur, Panvel, Kharghar, Belapur, Nerul, Vashi",
                priceMonthly = 1000.0,
                studentPriceMonthly = 500.0,
                duration = "1h 30m",
                timings = "06:45 AM, 08:15 AM, 01:30 PM, 05:00 PM, 08:00 PM"
            ),
            BusRoute(
                fromPlace = "Khalapur",
                toPlace = "Karjat",
                stops = "Khalapur, Chowk, Karjat Station",
                priceMonthly = 500.0,
                studentPriceMonthly = 250.0,
                duration = "35 mins",
                timings = "08:00 AM, 11:00 AM, 03:00 PM, 06:30 PM"
            ),
            BusRoute(
                fromPlace = "Khalapur",
                toPlace = "Rasayani",
                stops = "Khalapur, Wasambe, Pillai Campus, Rasayani",
                priceMonthly = 400.0,
                studentPriceMonthly = 200.0,
                duration = "25 mins",
                timings = "07:15 AM, 08:45 AM, 01:15 PM, 05:15 PM"
            )
        )

        for (route in sampleRoutes) {
            val values = ContentValues().apply {
                put(KEY_ROUTE_FROM, route.fromPlace)
                put(KEY_ROUTE_TO, route.toPlace)
                put(KEY_ROUTE_STOPS, route.stops)
                put(KEY_ROUTE_PRICE, route.priceMonthly)
                put(KEY_ROUTE_STUDENT_PRICE, route.studentPriceMonthly)
                put(KEY_ROUTE_DURATION, route.duration)
                put(KEY_ROUTE_TIMINGS, route.timings)
            }
            db.insert(TABLE_ROUTES, null, values)
        }
    }

    // --- USER OPERATIONS ---

    fun registerUser(user: User): Long {
        val db = writableDatabase
        // Check if email already exists
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(KEY_USER_ID),
            "$KEY_USER_EMAIL = ?",
            arrayOf(user.email.lowercase().trim()),
            null, null, null
        )
        val exists = cursor.moveToFirst()
        cursor.close()
        if (exists) return -1L // Email exists

        val values = ContentValues().apply {
            put(KEY_USER_NAME, user.name)
            put(KEY_USER_EMAIL, user.email.lowercase().trim())
            put(KEY_USER_PHONE, user.phone.trim())
            put(KEY_USER_ROLE, user.role.name)
            put(KEY_USER_COLLEGE, user.collegeName)
            put(KEY_USER_STUDENT_ID, user.studentId)
            put(KEY_USER_PASSWORD_HASH, user.passwordHash)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun loginUser(emailOrPhone: String, passwordHash: String): User? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE ($KEY_USER_EMAIL = ? OR $KEY_USER_PHONE = ?) AND $KEY_USER_PASSWORD_HASH = ?"
        val cleanInput = emailOrPhone.lowercase().trim()
        val cursor = db.rawQuery(query, arrayOf(cleanInput, cleanInput, passwordHash))
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = cursorToUser(cursor)
        }
        cursor.close()
        return user
    }

    fun getUserById(userId: Long): User? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS, null,
            "$KEY_USER_ID = ?", arrayOf(userId.toString()),
            null, null, null
        )
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = cursorToUser(cursor)
        }
        cursor.close()
        return user
    }

    fun updateUserProfile(user: User): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(KEY_USER_NAME, user.name)
            put(KEY_USER_PHONE, user.phone)
            if (user.role == UserRole.STUDENT) {
                put(KEY_USER_COLLEGE, user.collegeName)
                put(KEY_USER_STUDENT_ID, user.studentId)
            }
        }
        val rows = db.update(TABLE_USERS, values, "$KEY_USER_ID = ?", arrayOf(user.id.toString()))
        return rows > 0
    }

    fun updateUserPassword(userId: Long, newPasswordHash: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(KEY_USER_PASSWORD_HASH, newPasswordHash)
        }
        val rows = db.update(TABLE_USERS, values, "$KEY_USER_ID = ?", arrayOf(userId.toString()))
        return rows > 0
    }

    private fun cursorToUser(cursor: Cursor): User {
        val id = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_USER_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_NAME))
        val email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_EMAIL))
        val phone = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_PHONE))
        val roleStr = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_ROLE))
        val college = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_COLLEGE))
        val studentId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_STUDENT_ID))
        val passHash = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_PASSWORD_HASH))

        val role = try { UserRole.valueOf(roleStr) } catch (e: Exception) { UserRole.PASSENGER }

        return User(
            id = id,
            name = name,
            email = email,
            phone = phone,
            role = role,
            collegeName = college,
            studentId = studentId,
            passwordHash = passHash
        )
    }

    // --- ROUTE OPERATIONS ---

    fun getAllRoutes(): List<BusRoute> {
        val routes = mutableListOf<BusRoute>()
        val db = readableDatabase
        val cursor = db.query(TABLE_ROUTES, null, null, null, null, null, "$KEY_ROUTE_FROM ASC")
        while (cursor.moveToNext()) {
            routes.add(cursorToRoute(cursor))
        }
        cursor.close()
        return routes
    }

    fun getRouteById(routeId: Long): BusRoute? {
        val db = readableDatabase
        val cursor = db.query(TABLE_ROUTES, null, "$KEY_ROUTE_ID = ?", arrayOf(routeId.toString()), null, null, null)
        var route: BusRoute? = null
        if (cursor.moveToFirst()) {
            route = cursorToRoute(cursor)
        }
        cursor.close()
        return route
    }

    fun searchRoutes(query: String): List<BusRoute> {
        if (query.isBlank()) return getAllRoutes()
        val routes = mutableListOf<BusRoute>()
        val db = readableDatabase
        val q = "%${query.trim()}%"
        val sql = "SELECT * FROM $TABLE_ROUTES WHERE $KEY_ROUTE_FROM LIKE ? OR $KEY_ROUTE_TO LIKE ? OR $KEY_ROUTE_STOPS LIKE ?"
        val cursor = db.rawQuery(sql, arrayOf(q, q, q))
        while (cursor.moveToNext()) {
            routes.add(cursorToRoute(cursor))
        }
        cursor.close()
        return routes
    }

    private fun cursorToRoute(cursor: Cursor): BusRoute {
        return BusRoute(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ROUTE_ID)),
            fromPlace = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROUTE_FROM)),
            toPlace = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROUTE_TO)),
            stops = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROUTE_STOPS)),
            priceMonthly = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_ROUTE_PRICE)),
            studentPriceMonthly = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_ROUTE_STUDENT_PRICE)),
            duration = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROUTE_DURATION)),
            timings = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROUTE_TIMINGS))
        )
    }

    // --- PASS OPERATIONS ---

    fun createPass(busPass: BusPass): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(KEY_PASS_ID, busPass.id)
            put(KEY_PASS_USER_ID, busPass.userId)
            put(KEY_PASS_ROUTE_ID, busPass.routeId)
            put(KEY_PASS_START_DATE, busPass.startDate)
            put(KEY_PASS_END_DATE, busPass.endDate)
            put(KEY_PASS_STATUS, busPass.status.name)
            put(KEY_PASS_PAYMENT_STATUS, busPass.paymentStatus.name)
            put(KEY_PASS_CREATED_AT, busPass.createdAt)
            put(KEY_PASS_DURATION, busPass.durationMonths)
            put(KEY_PASS_PRICE_PAID, busPass.pricePaid)
        }
        val result = db.insert(TABLE_PASSES, null, values)
        return result != -1L
    }

    fun checkAndUpdateExpiredPasses() {
        val db = writableDatabase
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        // Mark passes as EXPIRED if endDate < todayStr and status == ACTIVE
        val sql = "UPDATE $TABLE_PASSES SET $KEY_PASS_STATUS = 'EXPIRED' WHERE $KEY_PASS_END_DATE < ? AND $KEY_PASS_STATUS = 'ACTIVE'"
        db.execSQL(sql, arrayOf(todayStr))
    }

    fun getUserPasses(userId: Long): List<BusPass> {
        checkAndUpdateExpiredPasses()
        val passes = mutableListOf<BusPass>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PASSES, null,
            "$KEY_PASS_USER_ID = ?", arrayOf(userId.toString()),
            null, null, "$KEY_PASS_CREATED_AT DESC"
        )
        while (cursor.moveToNext()) {
            passes.add(cursorToPass(cursor))
        }
        cursor.close()
        return passes
    }

    fun getActivePassForUser(userId: Long): BusPass? {
        checkAndUpdateExpiredPasses()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PASSES, null,
            "$KEY_PASS_USER_ID = ? AND $KEY_PASS_STATUS = 'ACTIVE' AND $KEY_PASS_PAYMENT_STATUS = 'PAID'",
            arrayOf(userId.toString()),
            null, null, "$KEY_PASS_END_DATE DESC"
        )
        var pass: BusPass? = null
        if (cursor.moveToFirst()) {
            pass = cursorToPass(cursor)
        }
        cursor.close()
        return pass
    }

    fun getPassById(passId: String): BusPass? {
        checkAndUpdateExpiredPasses()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PASSES, null,
            "$KEY_PASS_ID = ?", arrayOf(passId),
            null, null, null
        )
        var pass: BusPass? = null
        if (cursor.moveToFirst()) {
            pass = cursorToPass(cursor)
        }
        cursor.close()
        return pass
    }

    private fun cursorToPass(cursor: Cursor): BusPass {
        val id = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASS_ID))
        val userId = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_PASS_USER_ID))
        val routeId = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_PASS_ROUTE_ID))
        val startDate = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASS_START_DATE))
        val endDate = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASS_END_DATE))
        val statusStr = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASS_STATUS))
        val payStr = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASS_PAYMENT_STATUS))
        val createdAt = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASS_CREATED_AT))
        val durationMonths = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PASS_DURATION))
        val pricePaid = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PASS_PRICE_PAID))

        val status = try { PassStatus.valueOf(statusStr) } catch (e: Exception) { PassStatus.EXPIRED }
        val paymentStatus = try { PaymentStatus.valueOf(payStr) } catch (e: Exception) { PaymentStatus.PAID }

        return BusPass(
            id = id,
            userId = userId,
            routeId = routeId,
            startDate = startDate,
            endDate = endDate,
            status = status,
            paymentStatus = paymentStatus,
            createdAt = createdAt,
            durationMonths = durationMonths,
            pricePaid = pricePaid
        )
    }
}
