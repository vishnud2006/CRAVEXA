# CRAVEXA — CRAVE BETTER 🍲

> **A modern native Android marketplace connecting passionate home-based food creators with customers.**

CRAVEXA enables homemade food creators (pickles, spices, sweets, snacks, traditional foods, regional specialties, baked goods, festival hampers) to showcase and sell authentic homemade delicacies directly to food lovers.

---

## 📁 Project Architecture & Directory Structure

CRAVEXA is organized into clean, modular directories so that anyone can easily understand, navigate, and modify the project:

```
CRAVEXA/
│
├── frontend/                          # 📱 Native Android Client Application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/cravexa/
│   │   │   │   ├── CravexaApp.kt       # Application class (@HiltAndroidApp)
│   │   │   │   ├── MainActivity.kt     # Single Activity host with Edge-to-Edge Compose
│   │   │   │   ├── core/               # Shared utilities, Design System, DI, & Navigation
│   │   │   │   │   ├── common/         # Resource & UiState wrapper models
│   │   │   │   │   ├── constants/      # AppConstants (Base URL, Timeouts, Pref keys)
│   │   │   │   │   ├── designsystem/   # Design tokens (Colors, Typography, Shapes, Themes) & UI components
│   │   │   │   │   ├── di/             # Hilt Dependency Injection Modules (AppModule, AuthModule, etc.)
│   │   │   │   │   └── navigation/     # Jetpack Compose NavHost & Screen route definitions
│   │   │   │   ├── data/               # Data Layer (Local DataStore & Remote Retrofit API services)
│   │   │   │   │   ├── local/          # PreferenceManager (Encrypted DataStore tokens & session)
│   │   │   │   │   ├── remote/         # ApiService (Retrofit) & TokenInterceptor (OkHttp)
│   │   │   │   │   └── repository/     # Concrete repository implementations with sample/live data
│   │   │   │   ├── domain/             # Domain Layer (Business Logic & Entities)
│   │   │   │   │   ├── model/          # Clean domain data models (Product, Order, User, etc.)
│   │   │   │   │   ├── repository/     # Repository interfaces defining contracts
│   │   │   │   │   └── usecase/        # Independent business use cases (Cart, Orders, Auth)
│   │   │   │   └── presentation/       # Presentation Layer (UI Screens & ViewModels)
│   │   │   │       ├── admin/          # Admin portal screens (Dashboard, Sellers, Products, Orders)
│   │   │   │       ├── auth/           # Authentication (Login, Signup, Phone OTP, Profile Setup)
│   │   │   │       ├── cart/           # Shopping Cart & Price Breakdown
│   │   │   │       ├── checkout/       # Checkout, Payment options, & Order Confirmation
│   │   │   │       ├── customer/       # Customer Profile, Addresses, Orders & Live Tracking
│   │   │   │       ├── home/           # Home Screen with Hero Banners & Recommendations
│   │   │   │       ├── onboarding/     # Onboarding carousel & role selection
│   │   │   │       ├── product/        # Product detail, ingredients, & creator story
│   │   │   │       ├── search/         # Product search & filtering bottom sheet
│   │   │   │       ├── seller/         # Seller dashboard, product management, earnings, orders
│   │   │   │       ├── splash/         # Animated Splash & session routing
│   │   │   │       ├── welcome/        # Role selection screen (Customer vs. Seller)
│   │   │   │       └── wishlist/       # Saved favorite homemade delicacies
│   │   │   └── res/                    # Drawables, mipmaps, strings, colors, & themes
│   │   └── test/                       # 40+ JVM Unit Tests (ViewModels, Repositories, Use Cases)
│   ├── build.gradle.kts                # Frontend module build script
│   └── proguard-rules.pro              # Proguard optimization rules
│
├── backend/                           # 🌐 Backend Server & API Services
│   └── README.md                       # Backend architecture, API contracts, & setup instructions
│
├── gradle/                            # Gradle wrapper binaries & Version Catalog
│   ├── libs.versions.toml              # Centralized dependencies & plugin version catalog
│   └── wrapper/                        # Gradle wrapper JAR and properties
├── .gitignore                         # Git ignore configuration
├── build.gradle.kts                   # Root build configuration
├── settings.gradle.kts                # Project module mapping (:app -> frontend)
└── gradle.properties                  # JVM parameters and AndroidX flags
```

---

## 🛠️ Tech Stack

* **Language**: Kotlin 2.0.21
* **UI Toolkit**: Jetpack Compose (Material 3)
* **Architecture**: Clean Architecture + MVVM + Repository Pattern + Unidirectional Data Flow
* **Dependency Injection**: Dagger Hilt
* **Navigation**: Jetpack Compose Navigation
* **Asynchronous Programming**: Kotlin Coroutines & StateFlow
* **Local Storage**: Jetpack DataStore Preferences
* **Networking**: Retrofit 2 + OkHttp 4
* **Image Loading**: Coil Compose
* **Testing**: JUnit 4, MockK, Kotlinx Coroutines Test, Turbine

---

## 🧩 How to Modify and Extend CRAVEXA

Anyone can easily understand and modify the codebase by following these patterns:

### 1. Adding or Modifying a Screen (Frontend)
1. **Define the Route**: Open `frontend/src/main/java/com/cravexa/core/navigation/Screen.kt` and add your screen's route.
2. **Create the UI Composable**: In `frontend/src/main/java/com/cravexa/presentation/<feature>/`, create your Composable screen (e.g. `MyScreen.kt`).
3. **Create the ViewModel**: Create `MyViewModel.kt` annotated with `@HiltViewModel` to manage screen state via `StateFlow`.
4. **Register in NavHost**: Add a `composable(Screen.MyScreen.route)` destination in `frontend/src/main/java/com/cravexa/core/navigation/CravexaNavHost.kt`.

### 2. Connecting to a Backend Server
1. **Change the Base URL**: Open `frontend/src/main/java/com/cravexa/core/constants/AppConstants.kt` and update `BASE_URL`:
   ```kotlin
   const val BASE_URL = "https://your-backend-api.com/" // or "http://10.0.2.2:8000/" for local emulator
   ```
2. **Define API Endpoints**: In `frontend/src/main/java/com/cravexa/data/remote/ApiService.kt`, declare your HTTP endpoints using Retrofit annotations.
3. **Backend Service**: Follow the guide in [`backend/README.md`](backend/README.md) to implement the matching backend in Python, Node.js, or Java.

### 3. Modifying Theme & Design System
* Colors: `frontend/src/main/java/com/cravexa/core/designsystem/theme/Color.kt`
* Typography: `frontend/src/main/java/com/cravexa/core/designsystem/theme/Type.kt`
* Shapes & Elevation: `frontend/src/main/java/com/cravexa/core/designsystem/theme/Shape.kt`
* Reusable UI Components: `frontend/src/main/java/com/cravexa/core/designsystem/components/`

---

## 🚀 Building and Running

### Prerequisites
* **JDK**: Version 17+
* **Android Studio**: Ladybug / Koala / Hedgehog (or newer)
* **Android SDK**: API 35 (compileSdk), minimum Android 7.0 (API 24)

### Terminal Commands
```bash
# Build the Debug APK
./gradlew assembleDebug

# Run all unit tests
./gradlew test
```
