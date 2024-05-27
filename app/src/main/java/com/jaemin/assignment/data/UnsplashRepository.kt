package com.jaemin.assignment.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jaemin.assignment.api.UnsplashService
import com.jaemin.assignment.data.model.UnsplashPhoto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val unsplashService: UnsplashService
) {
    private val favoritesKey = stringSetPreferencesKey("favorites")

    val favoritePhotoIds: Flow<Set<String>> = dataStore.data.map { preferences ->
        preferences[favoritesKey] ?: emptySet()
    }

    suspend fun addFavorite(photoId: String) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[favoritesKey] ?: emptySet()
            preferences[favoritesKey] = currentFavorites + photoId
        }
    }

    suspend fun removeFavorite(photoId: String) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[favoritesKey] ?: emptySet()
            preferences[favoritesKey] = currentFavorites - photoId
        }
    }

    fun getUnsplashPhotosStream(query: String): Flow<PagingData<UnsplashPhoto>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE),
            pagingSourceFactory = { UnsplashPagingSource(unsplashService, query) }
        ).flow
    }

    companion object {
        private const val PAGING_SIZE = 30
    }
}