# CRAVEXA — CRAVE BETTER 🍲

> **A native Android marketplace connecting passionate home-based food creators with customers.**

CRAVEXA enables homemade food creators (pickles, spices, sweets, snacks, traditional foods, regional specialties, baked goods, festival food hampers) to showcase and sell authentic homemade delicacies to food lovers.

---

## 🛠️ Tech Stack & Architecture

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

## 📁 Project Structure

```
CRAVEXA/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/cravexa/
│   │   │   │   ├── CravexaApp.kt              # Application class (@HiltAndroidApp)
│   │   │   │   ├── MainActivity.kt            # Main ComponentActivity
│   │   │   │   ├── core/
│   │   │   │   │   ├── common/                # Resource & UiState models
│   │   │   │   │   ├── constants/             # AppConstants
│   │   │   │   │   ├── designsystem/          # Design tokens (Color, Type, Shape, Spacing, Theme) & Reusable Components
│   │   │   │   │   ├── di/                    # Hilt Dependency Injection Modules
│   │   │   │   │   └── navigation/            # Screens & Compose NavHost
│   │   │   │   ├── data/                      # Local DataStore & Remote API Services
│   │   │   │   ├── domain/                    # Entities, UserRole & Onboarding Models
│   │   │   │   └── presentation/              # Presentation screens (Splash, Onboarding, Home)
│   │   │   └── res/                           # Vector assets, strings, colors, XML configurations
│   │   └── test/                              # JVM Unit tests for ViewModels & business logic
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml                     # Gradle Version Catalog
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 🚀 Building and Running

### Prerequisites
* Android Studio (Ladybug / Koala / Hedgehog or newer)
* Android SDK (API 35, Build-Tools 34+)
* JDK 17+

### Terminal Build
```bash
# Build Debug APK
./gradlew assembleDebug

# Run Unit Tests
./gradlew test
```

