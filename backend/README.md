# CRAVEXA — Backend Services & API 🌐

This directory is designated for the CRAVEXA backend server, database, and API services.

---

## 🔌 Frontend Connection

The Android frontend communicates with the backend via REST APIs configured in:
* **Configuration:** `frontend/src/main/java/com/cravexa/core/constants/AppConstants.kt`
  * Default URL: `https://api.cravexa.com/`
  * Local Android Emulator: `http://10.0.2.2:8000/` (or your local port)
* **HTTP Client:** Retrofit 2 + OkHttp 4
* **Auth Protocol:** Bearer Token Authorization (`Authorization: Bearer <JWT_TOKEN>`)

---

## 📋 Recommended API Endpoints

To support all screens in the CRAVEXA frontend application, a backend service should provide the following RESTful routes:

### 1. Authentication (`/api/v1/auth`)
* `POST /login` — Authenticate user (Email & Password) -> returns `{ token, user }`
* `POST /signup` — Register new user (Customer or Seller)
* `POST /phone-login` — Send OTP / verify OTP for phone-based login
* `POST /forgot-password` — Password reset link/OTP

### 2. Products & Categories (`/api/v1/products`)
* `GET /` — List products (supports query params: `search`, `category`, `sort`, `minPrice`, `maxPrice`)
* `GET /{id}` — Get single product details, ingredients, creator info
* `GET /categories` — List available food categories (Pickles, Spices, Sweets, Snacks, etc.)
* `GET /featured-banners` — List hero promotional banners for the home screen

### 3. Orders & Checkout (`/api/v1/orders`)
* `POST /` — Place a new customer order
* `GET /` — List order history for the logged-in user
* `GET /{id}` — Get detailed order summary & line items
* `GET /{id}/tracking` — Live tracking milestones (Ordered, Preparing, Dispatched, Delivered)

### 4. Cart & Wishlist (`/api/v1/cart`, `/api/v1/wishlist`)
* `GET /`, `POST /add`, `DELETE /{id}` — Manage server-synced cart & wishlist

### 5. Seller Portal (`/api/v1/seller`)
* `GET /dashboard` — Summary stats (revenue, pending orders, total sales)
* `GET /products` — List seller's products
* `POST /products` — Create / upload a new homemade product listing
* `PUT /products/{id}` — Update price, stock, or description
* `GET /orders` — Orders received for this seller
* `GET /earnings` — Payout history and available balance
* `GET /reviews` — Customer reviews and ratings

### 6. Admin Portal (`/api/v1/admin`)
* `GET /dashboard` — System-wide analytics
* `GET /users`, `GET /sellers` — User & seller management
* `POST /sellers/{id}/verify-fssai` — FSSAI license approval/rejection
* `GET /complaints` — Customer complaints & dispute resolution

---

## 🛠️ Recommended Backend Stacks

You can build the backend in this directory using any framework of your choice:
1. **Python**: FastAPI or Django REST Framework
2. **Node.js**: Express.js or NestJS (TypeScript)
3. **Java / Kotlin**: Spring Boot or Ktor
4. **Go**: Gin or Fiber
