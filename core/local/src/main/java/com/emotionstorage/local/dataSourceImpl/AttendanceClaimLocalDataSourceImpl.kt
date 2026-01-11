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
    private val Context.attendanceDataStore by preferencesDataStore(name = "attendance_prefs")

    override suspend fun getLastClaimDate(): String? =
        try {
            context.attendanceDataStore.data.first()[KEY_LAST_CLAIM_DATE]
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    override suspend fun setLastClaimDate(date: String) {
        try {
            context.attendanceDataStore.edit { prefs ->
                prefs[KEY_LAST_CLAIM_DATE] = date
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun clear() {
        try {
            context.attendanceDataStore.edit { prefs ->
                prefs.remove(KEY_LAST_CLAIM_DATE)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private val KEY_LAST_CLAIM_DATE = stringPreferencesKey("last_claim_date")
    }
}
