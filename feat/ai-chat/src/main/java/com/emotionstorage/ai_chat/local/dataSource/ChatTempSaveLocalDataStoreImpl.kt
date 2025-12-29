package com.emotionstorage.ai_chat.local.dataSource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.emotionstorage.ai_chat.data.dataSource.local.ChatTempSaveLocalDataSource
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatTempSaveLocalDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : ChatTempSaveLocalDataSource {

    private object Keys {
        val TEMP_SAVED_ROOM_ID = androidx.datastore.preferences.core.longPreferencesKey("temp_saved_room_id")
    }

    override val tempSavedRoomId: kotlinx.coroutines.flow.Flow<Long?> =
        dataStore.data.map { prefs -> prefs[Keys.TEMP_SAVED_ROOM_ID] }

    override suspend fun setTempSavedRoomId(roomId: Long) {
        dataStore.edit { prefs -> prefs[Keys.TEMP_SAVED_ROOM_ID] = roomId }
    }

    override suspend fun clearTempSavedRoomId() {
        dataStore.edit { prefs -> prefs.remove(Keys.TEMP_SAVED_ROOM_ID) }
    }
}
