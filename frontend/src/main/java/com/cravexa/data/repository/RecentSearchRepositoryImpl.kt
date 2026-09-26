package com.cravexa.data.repository

import com.cravexa.data.local.PreferenceManager
import com.cravexa.domain.repository.RecentSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentSearchRepositoryImpl @Inject constructor(
    private val preferenceManager: PreferenceManager
) : RecentSearchRepository {

    override fun getRecentSearches(): Flow<List<String>> = preferenceManager.recentSearches

    override suspend fun addRecentSearch(query: String) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return
        val current = preferenceManager.recentSearches.first().toMutableList()
        current.removeAll { it.equals(trimmed, ignoreCase = true) }
        current.add(0, trimmed)
        val capped = current.take(10)
        preferenceManager.saveRecentSearches(capped)
    }

    override suspend fun removeRecentSearch(query: String) {
        val current = preferenceManager.recentSearches.first().toMutableList()
        current.removeAll { it.equals(query, ignoreCase = true) }
        preferenceManager.saveRecentSearches(current)
    }

    override suspend fun clearRecentSearches() {
        preferenceManager.saveRecentSearches(emptyList())
    }
}

