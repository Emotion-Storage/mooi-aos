package com.emotionstorage.local.dataSourceImpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.emotionstorage.data.dataSource.local.ChatTempSaveLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatTempSaveLocalDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : ChatTempSaveLocalDataSource {
    private object Keys {
        val TEMP_SAVED_ROOM_ID =
            longPreferencesKey("temp_saved_room_id")
    }

    override val tempSavedRoomId: Flow<Long?> =
        dataStore.data.map { prefs -> prefs[Keys.TEMP_SAVED_ROOM_ID] }

    override suspend fun setTempSavedRoomId(roomId: Long) {
        dataStore.edit { prefs -> prefs[Keys.TEMP_SAVED_ROOM_ID] = roomId }
    }

    override suspend fun clearTempSavedRoomId() {
        dataStore.edit { prefs -> prefs.remove(Keys.TEMP_SAVED_ROOM_ID) }
    }
}
