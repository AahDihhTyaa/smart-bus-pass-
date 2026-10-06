# 🚌 Smart Digital Bus Pass - Android Application

A complete, paperless, offline-ready Android app built for a college project. The app enables college students and city passengers to register, view bus routes/timings, apply for bus passes with instant student discounts, issue digital passes with custom canvas-rendered QR codes, download passes to device gallery, and manage pass history.

---

## 🛠️ Tech Stack (100% Stock Android Studio - Zero External Dependencies)
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Database**: Built-in Android `SQLiteOpenHelper` (`android.database.sqlite`)
- **Authentication**: Local SQLite storage with SHA-256 password hashing (`java.security.MessageDigest`)
- **Session Management**: Android `SharedPreferences`
- **Navigation**: Lightweight state-based screen switching (`sealed class Screen` + `mutableStateOf`)
- **QR Code Generation**: Built-in Compose `Canvas` drawing deterministic QR grid with finder patterns from pass ID seed (Zero 3rd party QR libraries)
- **Download Pass**: Built-in Bitmap canvas export + `MediaStore.Images` gallery saver
- **Share Pass**: Native Android `Intent.ACTION_SEND`
- **Payment**: Built-in Mock Payment screen (UPI & Cash options)
- **Icons**: Self-contained Compose Vector Painters & built-in Material design vectors

---

## 🚀 How to Run the Project

### Prerequisites
- Stock **Android Studio** (Ladybug / Jellyfish / Iguana or later)
- JDK 17 (Included with Android Studio)
- Android SDK Platform API 34/35/36 installed

### Step 1: Open Project in Android Studio
1. Launch Android Studio.
2. Click **Open** and select the project folder: `Smart Digital Bus Pass`.
3. Wait for Gradle to finish initial indexing and Sync (no internet or API keys required).

### Step 2: Run on an Android Emulator
1. Click **Tools > Device Manager** in Android Studio.
2. Click **Create Virtual Device** (e.g. Pixel 7 / Pixel 8 running Android 11+ / API 30+).
3. Start the emulator.
4. Select the running emulator from the device dropdown at the top toolbar.
5. Click the green **Run** button (`Shift + F10`).

### Step 3: Run on a Real Android Phone
1. Enable **Developer Options** on your phone:
   - Go to **Settings > About Phone** -> Tap **Build Number** 7 times.
2. Enable **USB Debugging**:
   - Go to **Settings > Developer Options** -> Enable **USB Debugging**.
3. Connect your phone to your PC via USB cable and allow USB Debugging prompt on phone screen.
4. Select your physical device from Android Studio top toolbar and click **Run**.

---

## 👥 User Roles & Features

### 1. Student Role
- Requires: Name, Email/Mobile, Password, College Name, Student ID / PRN No.
- **Benefits**: Automatically receives **50% concession** on all monthly bus routes.

### 2. Normal Passenger Role
- Requires: Name, Email/Mobile, Password.
- **Benefits**: Standard monthly rates for hassle-free daily commuting.

---

## 📱 App Screens Walkthrough

1. **Splash Screen**: Logo animation + auto-redirection to Login or Home based on active session.
2. **Login / Register Screen**:
   - Role Selection Tabs: **Student** | **Passenger**.
   - Input validation, password show/hide toggle, SHA-256 local hash authentication.
3. **Home Screen**:
   - Personalized greeting with role badge.
   - **Active Pass Card**: Displays active pass ID, route, validity end date, VALID status badge, and mini QR preview.
   - **Quick Action Grid**: Apply New Pass, My Pass, Route & Timings, Pass History, Profile, Bus Pass Info.
   - **Bottom Navigation Bar**: Persistent navigation across main tabs.
4. **Bus Pass Info & Rules Screen**: Details pass types, student discounts, required documents, and usage rules.
5. **Apply for Pass (3-Step Stepper Flow)**:
   - *Step 1: Select Route*: Interactive list of 6 pre-loaded sample routes with monthly fares and duration.
   - *Step 2: Details & Duration*: Displays user category, auto-calculated discount price, 1-Month vs 3-Months pass duration selector.
   - *Step 3: Mock Payment*: Select UPI or Cash at Depot -> Process payment -> Instantly creates active pass in SQLite database.
6. **Digital Bus Pass Screen**:
   - Full digital ticket displaying passenger name, PRN, route, validity dates, status badge.
   - Large custom **Compose Canvas QR Code**.
   - **Save Pass Button**: Exports high-resolution ticket image directly to phone gallery (`Pictures/SmartDigitalBusPass`).
   - **Share Button**: Shares pass details via WhatsApp, SMS, or Email.
7. **Route & Timings Screen**: Searchable list of bus routes with stops, timings, regular price, and student discounted price.
8. **Pass History Screen**: Chronological record of all past and current passes with ACTIVE / EXPIRED badges.
9. **Profile Screen**: View/edit profile details, change password (SHA-256), and log out.
10. **About & Help Screen**: Application details, FAQs, and developer notes.

---

## 💾 Database Schema (SQLite)

- **`users`**: `id`, `name`, `email`, `phone`, `role`, `collegeName`, `studentId`, `passwordHash`
- **`routes`**: `id`, `fromPlace`, `toPlace`, `stops`, `priceMonthly`, `studentPriceMonthly`, `duration`, `timings`
- **`passes`**: `id`, `userId`, `routeId`, `startDate`, `endDate`, `status`, `paymentStatus`, `createdAt`, `durationMonths`, `pricePaid`

### Pre-loaded Sample Routes
1. **Khalapur to Mumbai** (Stops: Khalapur, Chowk, Panvel, Vashi, Dadar, Mumbai CSMT | Fare: ₹1200 / Student: ₹600)
2. **Khalapur to Panvel** (Stops: Khalapur, Shedung, Chowk, Old Panvel, Panvel Station | Fare: ₹600 / Student: ₹300)
3. **Khalapur to Taloja** (Stops: Khalapur, Mohopada, Reshim, Taloja MIDC | Fare: ₹750 / Student: ₹375)
4. **Khalapur to Navi Mumbai** (Stops: Khalapur, Panvel, Kharghar, Belapur, Nerul, Vashi | Fare: ₹1000 / Student: ₹500)
5. **Khalapur to Karjat** (Stops: Khalapur, Chowk, Karjat Station | Fare: ₹500 / Student: ₹250)
6. **Khalapur to Rasayani** (Stops: Khalapur, Wasambe, Pillai Campus, Rasayani | Fare: ₹400 / Student: ₹200)

---

## 📌 Technical Assumptions Made
1. **Pass Expiry Check**: Pass expiry is calculated dynamically against local system date (`YYYY-MM-DD`). Passes whose end date is past today's date are automatically updated to `EXPIRED` in the database.
2. **Offline-First Storage**: All data persists in local SQLite storage (`smart_bus_pass.db`). Clearing app data will reset database to fresh seeded state.
3. **Mock Payment**: Payment in Step 3 simulates successful instant verification (no external bank SDKs needed).
4. **QR Matrix**: QR code matrix is generated deterministically using a seeded pseudo-random grid combined with standard QR corner finder patterns derived from the unique `passId.hashCode()`.
