package com.emotionstorage.ai_chat.local.dataSource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.emotionstorage.ai_chat.data.dataSource.local.AiChatIntroLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AiChatIntroLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : AiChatIntroLocalDataSource {
    companion object {
        private val KEY = booleanPreferencesKey("ai_chat_intro_seen")
    }

    override fun observeIntroSeen(): Flow<Boolean> = dataStore.data.map { it[KEY] ?: false }

    override suspend fun markIntroSeen(value: Boolean) {
        dataStore.edit { it[KEY] = value }
    }
}
