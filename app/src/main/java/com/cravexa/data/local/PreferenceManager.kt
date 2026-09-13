package com.cravexa.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cravexa.core.constants.AppConstants
import com.cravexa.domain.model.Address
import com.cravexa.domain.model.CartItem
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.Product
import com.cravexa.domain.model.SellerProfile
import com.cravexa.domain.model.UserProfile
import com.cravexa.domain.model.UserRole
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AppConstants.PREFS_NAME)

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val gson = Gson()
    private val keyOnboardingCompleted = booleanPreferencesKey(AppConstants.KEY_ONBOARDING_COMPLETED)
    private val keyAuthToken = stringPreferencesKey(AppConstants.KEY_AUTH_TOKEN)
    private val keyUserRole = stringPreferencesKey(AppConstants.KEY_USER_ROLE)
    private val keyUserId = stringPreferencesKey(AppConstants.KEY_USER_ID)
    private val keyUserProfileJson = stringPreferencesKey(AppConstants.KEY_USER_PROFILE_JSON)
    private val keyAddressesJson = stringPreferencesKey(AppConstants.KEY_ADDRESSES_JSON)
    private val keySellerProfileJson = stringPreferencesKey(AppConstants.KEY_SELLER_PROFILE_JSON)
    private val keyWishlistJson = stringPreferencesKey(AppConstants.KEY_WISHLIST_JSON)
    private val keyCartJson = stringPreferencesKey(AppConstants.KEY_CART_JSON)
    private val keyRecentSearchesJson = stringPreferencesKey(AppConstants.KEY_RECENT_SEARCHES_JSON)
    private val keyUsersRegistryJson = stringPreferencesKey("prefs_users_registry_json")
    private val keyOrdersJson = stringPreferencesKey("prefs_orders_json")

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[keyOnboardingCompleted] ?: false
        }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[keyOnboardingCompleted] = completed
        }
    }

    val authToken: Flow<String?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[keyAuthToken]
        }

    suspend fun saveAuthToken(token: String?) {
        context.dataStore.edit { preferences ->
            if (token != null) {
                preferences[keyAuthToken] = token
            } else {
                preferences.remove(keyAuthToken)
            }
        }
    }

    val userRole: Flow<UserRole> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            UserRole.fromString(preferences[keyUserRole])
        }

    suspend fun saveUserRole(role: UserRole) {
        context.dataStore.edit { preferences ->
            preferences[keyUserRole] = role.name
        }
    }

    val userId: Flow<String?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[keyUserId]
        }

    suspend fun saveUserId(userId: String?) {
        context.dataStore.edit { preferences ->
            if (userId != null) {
                preferences[keyUserId] = userId
            } else {
                preferences.remove(keyUserId)
            }
        }
    }

    val userProfile: Flow<UserProfile?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            val json = preferences[keyUserProfileJson]
            if (json.isNullOrEmpty()) null else {
                try {
                    gson.fromJson(json, UserProfile::class.java)
                } catch (e: Exception) {
                    null
                }
            }
        }

    val usersRegistry: Flow<Map<String, UserProfile>> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            val json = preferences[keyUsersRegistryJson]
            if (json.isNullOrEmpty()) emptyMap() else {
                try {
                    val type = object : TypeToken<Map<String, UserProfile>>() {}.type
                    gson.fromJson(json, type) ?: emptyMap()
                } catch (e: Exception) {
                    emptyMap()
                }
            }
        }

    suspend fun saveUserToRegistry(profile: UserProfile) {
        context.dataStore.edit { preferences ->
            val currentMap: MutableMap<String, UserProfile> = try {
                val json = preferences[keyUsersRegistryJson]
                if (json.isNullOrEmpty()) mutableMapOf() else {
                    val type = object : TypeToken<MutableMap<String, UserProfile>>() {}.type
                    gson.fromJson(json, type) ?: mutableMapOf()
                }
            } catch (e: Exception) {
                mutableMapOf()
            }
            currentMap[profile.firebaseUid] = profile
            if (profile.email.isNotBlank()) {
                currentMap["email_${profile.email.lowercase().trim()}"] = profile
            }
            if (profile.phone.isNotBlank()) {
                currentMap["phone_${profile.phone.filter { it.isDigit() }}"] = profile
            }
            preferences[keyUsersRegistryJson] = gson.toJson(currentMap)
        }
    }

    suspend fun getUserFromRegistry(uidOrEmail: String): UserProfile? {
        val currentRegistry = usersRegistry.map { it }.firstOrNull() ?: emptyMap()
        return currentRegistry[uidOrEmail] 
            ?: currentRegistry["email_${uidOrEmail.lowercase().trim()}"]
            ?: currentRegistry["phone_${uidOrEmail.filter { it.isDigit() }}"]
    }

    suspend fun saveUserProfile(profile: UserProfile?) {
        context.dataStore.edit { preferences ->
            if (profile != null) {
                preferences[keyUserProfileJson] = gson.toJson(profile)
                preferences[keyUserId] = profile.id
                preferences[keyUserRole] = profile.role.name
            } else {
                preferences.remove(keyUserProfileJson)
            }
        }
        if (profile != null) {
            saveUserToRegistry(profile)
        }
    }

    val addresses: Flow<List<Address>> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            val json = preferences[keyAddressesJson]
            if (json.isNullOrEmpty()) emptyList() else {
                try {
                    val type = object : TypeToken<List<Address>>() {}.type
                    gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            }
        }

    suspend fun saveAddresses(addressesList: List<Address>) {
        context.dataStore.edit { preferences ->
            preferences[keyAddressesJson] = gson.toJson(addressesList)
        }
    }

    val sellerProfile: Flow<SellerProfile?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            val json = preferences[keySellerProfileJson]
            if (json.isNullOrEmpty()) null else {
                try {
                    gson.fromJson(json, SellerProfile::class.java)
                } catch (e: Exception) {
                    null
                }
            }
        }

    suspend fun saveSellerProfile(profile: SellerProfile?) {
        context.dataStore.edit { preferences ->
            if (profile != null) {
                preferences[keySellerProfileJson] = gson.toJson(profile)
            } else {
                preferences.remove(keySellerProfileJson)
            }
        }
    }

    val wishlist: Flow<List<Product>> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            val json = preferences[keyWishlistJson]
            if (json.isNullOrEmpty()) emptyList() else {
                try {
                    val type = object : TypeToken<List<Product>>() {}.type
                    gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            }
        }

    suspend fun saveWishlist(products: List<Product>) {
        context.dataStore.edit { preferences ->
            preferences[keyWishlistJson] = gson.toJson(products)
        }
    }

    val cartItems: Flow<List<CartItem>> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            val json = preferences[keyCartJson]
            if (json.isNullOrEmpty()) emptyList() else {
                try {
                    val type = object : TypeToken<List<CartItem>>() {}.type
                    gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            }
        }

    suspend fun saveCartItems(items: List<CartItem>) {
        context.dataStore.edit { preferences ->
            preferences[keyCartJson] = gson.toJson(items)
        }
    }

    val recentSearches: Flow<List<String>> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            val json = preferences[keyRecentSearchesJson]
            if (json.isNullOrEmpty()) emptyList() else {
                try {
                    val type = object : TypeToken<List<String>>() {}.type
                    gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            }
        }

    suspend fun saveRecentSearches(searches: List<String>) {
        context.dataStore.edit { preferences ->
            preferences[keyRecentSearchesJson] = gson.toJson(searches)
        }
    }

    val orders: Flow<List<Order>> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            val json = preferences[keyOrdersJson]
            if (json.isNullOrEmpty()) emptyList() else {
                try {
                    val type = object : TypeToken<List<Order>>() {}.type
                    gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            }
        }

    suspend fun saveOrders(ordersList: List<Order>) {
        context.dataStore.edit { preferences ->
            preferences[keyOrdersJson] = gson.toJson(ordersList)
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(keyAuthToken)
            preferences.remove(keyUserId)
            preferences.remove(keyUserRole)
            preferences.remove(keyUserProfileJson)
            preferences.remove(keyAddressesJson)
            preferences.remove(keySellerProfileJson)
            preferences.remove(keyWishlistJson)
            preferences.remove(keyCartJson)
            preferences.remove(keyRecentSearchesJson)
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
