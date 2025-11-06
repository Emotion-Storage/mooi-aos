package com.emotionstorage.local.dataSourceImpl

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.emotionstorage.data.dataSource.local.FcmLocalDataSource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FcmLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : FcmLocalDataSource {
    companion object {
        private val KEY = stringPreferencesKey("fcm_token")
    }

    override suspend fun saveToken(token: String): Boolean {
        dataStore.edit { preferences ->
            preferences[KEY] = token
        }
        return true
    }

    override suspend fun getToken(): String? {
        return dataStore
            .data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[KEY]
            }.firstOrNull()
    }
}
