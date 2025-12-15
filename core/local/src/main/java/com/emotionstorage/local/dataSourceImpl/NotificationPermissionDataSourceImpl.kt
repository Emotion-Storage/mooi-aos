package com.emotionstorage.local.dataSourceImpl

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.emotionstorage.data.dataSource.local.NotificationPermissionDataSource
import com.emotionstorage.domain.model.NotificationPermissionInfo
import com.emotionstorage.domain.model.NotificationPermissionStatus
import com.orhanobut.logger.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

class NotificationPermissionDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : NotificationPermissionDataSource {
    private val Context.dataStore by preferencesDataStore("permission_prefs")

    private object Keys {
        val STATUS = stringPreferencesKey("notif_status")
        val PROMPTED = booleanPreferencesKey("notif_prompted")
    }

    override fun observeInfo(): Flow<NotificationPermissionInfo> =
        context.dataStore.data
            .catch { emit(emptyPreferences()) }
            .map { pref ->
                val statusName = pref[Keys.STATUS] ?: NotificationPermissionStatus.Unknown.name
                val status = NotificationPermissionStatus.entries.firstOrNull { it.name == statusName }
                    ?: NotificationPermissionStatus.Unknown
                val prompted = pref[Keys.PROMPTED] ?: false
                NotificationPermissionInfo(status = status, hasPrompted = prompted)
            }

    override suspend fun updateStatus(status: NotificationPermissionStatus) {
        try {
            context.dataStore.edit { it[Keys.STATUS] = status.name }
        } catch (e: IOException) {
            Logger.e("Update Notification Setting value Error ${e.message.toString()}")
        }
    }

    override suspend fun setPrompted(value: Boolean) {
        try {
            context.dataStore.edit { it[Keys.PROMPTED] = value }
        } catch (e: IOException) {
            Logger.e("Update Notification Setting value Error ${e.message.toString()}")
        }
    }
}
