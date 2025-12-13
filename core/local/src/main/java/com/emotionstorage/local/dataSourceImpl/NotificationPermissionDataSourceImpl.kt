package com.emotionstorage.local.dataSourceImpl

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.emotionstorage.data.dataSource.local.NotificationPermissionDataSource
import com.emotionstorage.domain.model.NotificationPermissionInfo
import com.emotionstorage.domain.model.NotificationPermissionStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationPermissionDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : NotificationPermissionDataSource {
    private val Context.dataStore by preferencesDataStore("permission_prefs")

    private object Keys {
        val STATUS = intPreferencesKey("notif_status")
        val PROMPTED = booleanPreferencesKey("notif_prompted")
    }

    override fun observeInfo(): Flow<NotificationPermissionInfo> =
        context.dataStore.data.map { pref ->
            val statusOrdinal = pref[Keys.STATUS] ?: NotificationPermissionStatus.Unknown.ordinal
            val status =
                NotificationPermissionStatus.entries.getOrElse(statusOrdinal) { NotificationPermissionStatus.Unknown }
            val prompted = pref[Keys.PROMPTED] ?: false
            NotificationPermissionInfo(status = status, hasPrompted = prompted)
        }

    override suspend fun updateStatus(status: NotificationPermissionStatus) {
        context.dataStore.edit { it[Keys.STATUS] = status.ordinal }
    }

    override suspend fun setPrompted(value: Boolean) {
        context.dataStore.edit { it[Keys.PROMPTED] = value }
    }
}
