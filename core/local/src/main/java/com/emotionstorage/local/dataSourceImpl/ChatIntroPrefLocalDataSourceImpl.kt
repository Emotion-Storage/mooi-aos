package com.emotionstorage.local.dataSourceImpl

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import com.emotionstorage.data.dataSource.ChatIntroPrefsLocalDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// TODO 옮기기
class ChatIntroPrefLocalDataSourceImpl @Inject constructor(
    @ApplicationContext context: Context,
) : ChatIntroPrefsLocalDataSource {
    companion object {
        const val PREF_NAME = "chat_intro_prefs"
    }

    private val dataStore =
        PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(PREF_NAME)
        }
    val key = booleanPreferencesKey("ai_chat_intro_seen")

    override val introSeen =
        dataStore.data.map {
            it[key] ?: false
        }

    override suspend fun setIntroSeen(value: Boolean) {
        dataStore.edit { it[key] = value }
    }
}
