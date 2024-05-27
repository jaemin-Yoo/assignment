package com.jaemin.assignment.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val favoritesKey = stringSetPreferencesKey("favorites")

    val favoriteImageIds: Flow<Set<String>> = dataStore.data.map { preferences ->
        preferences[favoritesKey] ?: emptySet()
    }

    suspend fun addFavorite(imageId: String) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[favoritesKey] ?: emptySet()
            preferences[favoritesKey] = currentFavorites + imageId
        }
    }

    suspend fun removeFavorite(imageId: String) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[favoritesKey] ?: emptySet()
            preferences[favoritesKey] = currentFavorites - imageId
        }
    }
}