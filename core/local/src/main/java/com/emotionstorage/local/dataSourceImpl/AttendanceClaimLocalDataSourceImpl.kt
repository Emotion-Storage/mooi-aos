package com.emotionstorage.local.dataSourceImpl

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.emotionstorage.data.dataSource.local.AttendanceClaimLocalDataSource
import com.orhanobut.logger.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

private val Context.attendanceDataStore by preferencesDataStore(name = "attendance_prefs")

class AttendanceClaimLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AttendanceClaimLocalDataSource {
    override suspend fun getLastClaimDate(): String? =
        context
            .attendanceDataStore
            .data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }.map { prefs -> prefs[KEY_LAST_CLAIM_DATE] }
            .first()

    override suspend fun setLastClaimDate(date: String) {
        try {
            context.attendanceDataStore.edit { prefs ->
                prefs[KEY_LAST_CLAIM_DATE] = date
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Failed setLastClaimDate")
        }
    }

    override suspend fun clear() {
        try {
            context.attendanceDataStore.edit { prefs ->
                prefs.remove(KEY_LAST_CLAIM_DATE)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Failed clear preferences")
        }
    }

    companion object {
        private val KEY_LAST_CLAIM_DATE = stringPreferencesKey("last_claim_date")
    }
}
