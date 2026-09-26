# CRAVEXA — Frontend (Android Client) 📱

This directory contains the entire native Android mobile application built with **Jetpack Compose** and **Clean Architecture**.

---

## 🏗️ Architecture Layers

The source code in `src/main/java/com/cravexa/` is separated into 4 distinct layers:

### 1. `presentation/` (UI Layer)
Contains all user-facing Jetpack Compose screens, ViewModels, and UI states.
* **`admin/`**: Admin portal (Complaints, Dashboard, FSSAI Verification, Orders, Payments, Products, Sellers, Users).
* **`auth/`**: Login, Signup, Phone OTP verification, Forgot Password, and Profile Setup.
* **`cart/`**: Shopping cart management and item count updates.
* **`category/`**: Category-wise product browsing.
* **`checkout/`**: Address selection, payment mode selection, and order placement.
* **`customer/`**: Customer main navigation, saved delivery addresses, order history, and live tracking.
* **`explore/`**: Explore and browse regional food specialties.
* **`home/`**: Home screen with hero promotional banners and curated lists.
* **`onboarding/`**: Welcome walkthrough introducing the marketplace.
* **`product/`**: Detailed product screen (ingredients, homemade story, shelf life).
* **`search/`**: Full-text search with filtering bottom sheet.
* **`seller/`**: Complete seller portal (Dashboard, Add/Edit Products, Orders, Earnings, Reviews, Public Seller Profile).
* **`splash/`**: Splash animation with automatic session restoration.
* **`welcome/`**: Role choice screen (Customer vs. Seller).
* **`wishlist/`**: Favorite items list.

### 2. `domain/` (Business Logic Layer)
Framework-independent pure Kotlin models, repository interfaces, and use cases:
* **`model/`**: Data models (`Product`, `Order`, `UserProfile`, `SellerProfile`, `HeroBanner`, `CartItem`, etc.).
* **`repository/`**: Interfaces declaring data access contracts (e.g. `ProductRepository`, `AuthRepository`).
* **`usecase/`**: Discrete business logic operations (`CreateOrderUseCase`, `CartCalculator`, `AuthUseCases`).

### 3. `data/` (Data Access Layer)
Concrete implementations of repository interfaces:
* **`local/`**: `PreferenceManager` using Android Jetpack DataStore Preferences to securely store auth tokens and user session data.
* **`remote/`**: `ApiService` (Retrofit HTTP client) and `TokenInterceptor` (OkHttp interceptor attaching Bearer JWT tokens).
* **`repository/`**: Repository implementations (`ProductRepositoryImpl`, `CartRepositoryImpl`, etc.) providing realistic sample data and bridging to remote APIs.

### 4. `core/` (Cross-Cutting Concerns)
* **`common/`**: `Resource<T>` sealed class (`Success`, `Error`, `Loading`) and `UiState<T>`.
* **`constants/`**: `AppConstants` (Centralized configuration: `BASE_URL`, timeouts, preference keys).
* **`designsystem/`**: Design tokens (`Color`, `Type`, `Shape`, `Spacing`, `Theme`) and reusable UI components (`CravexaButton`, `ProductCard`, `CravexaTopAppBar`, etc.).
* **`di/`**: Dagger Hilt dependency injection modules (`AppModule`, `AuthModule`, `Phase3Module`, etc.).
* **`navigation/`**: `Screen.kt` (all route definitions) and `CravexaNavHost.kt` (the single Compose NavHost).

---

## 🧪 Testing

All unit tests are located in `src/test/java/com/cravexa/`:
* Includes tests for ViewModels, Use Cases, Repositories, and Business Calculations.
* Run tests with:
  ```bash
  ./gradlew test
  ```
