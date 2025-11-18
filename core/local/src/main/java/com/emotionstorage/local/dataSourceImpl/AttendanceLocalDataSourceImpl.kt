package com.emotionstorage.local.dataSourceImpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.emotionstorage.data.dataSource.local.AttendanceLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class AttendanceLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : AttendanceLocalDataSource {
    companion object {
        private val LAST_CLAIM_DATE = stringPreferencesKey("attendance_last_claim_date")
        private val STREAK_COUNT = intPreferencesKey("attendance_streak_count")
        private val DIALOG_SHOWN = stringPreferencesKey("attendance_dialog_shown_date")
    }

    override fun wasDialogShown(date: LocalDate): Flow<Boolean> =
        dataStore.data.map { it[DIALOG_SHOWN] == date.toString() }

    override suspend fun setDialogShown(date: LocalDate) {
        dataStore.edit { it[DIALOG_SHOWN] = date.toString() }
    }

    // TODO : Local에서 확인하기 위함 필요 없을 시 삭제
    override fun streak(): Flow<Int> = dataStore.data.map { it[STREAK_COUNT] ?: 0 }

    override fun lastClaimDate(): Flow<String?> = dataStore.data.map { it[LAST_CLAIM_DATE] }

    // TODO : 계산을 위한 임시 함수
    suspend fun saveClaimToday(today: LocalDate) {
        dataStore.edit { p ->
            val last = p[LAST_CLAIM_DATE]?.let(LocalDate::parse)
            val nextStreak =
                when {
                    last == today -> p[STREAK_COUNT] ?: 0
                    last == today.minusDays(1) -> ((p[STREAK_COUNT] ?: 0) % 7) + 1
                    else -> 1
                }
            p[LAST_CLAIM_DATE] = today.toString()
            p[STREAK_COUNT] = nextStreak
        }
    }
}
