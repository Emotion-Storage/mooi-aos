package com.emotionstorage.local.dataSourceImpl

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.emotionstorage.data.dataSource.local.AttendanceClaimLocalDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AttendanceClaimLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AttendanceClaimLocalDataSource {
    override suspend fun getLastClaimDate(): String? = context.attendanceDataStore.data.first()[KEY_LAST_CLAIM_DATE]

    override suspend fun setLastClaimDate(date: String) {
        context.attendanceDataStore.edit { prefs ->
            prefs[KEY_LAST_CLAIM_DATE] = date
        }
    }

    override suspend fun clear() {
        context.attendanceDataStore.edit { prefs ->
            prefs.remove(KEY_LAST_CLAIM_DATE)
        }
    }

    companion object {
        private val Context.attendanceDataStore by preferencesDataStore(name = "attendance_prefs")
        private val KEY_LAST_CLAIM_DATE = stringPreferencesKey("last_claim_date")
    }
}
