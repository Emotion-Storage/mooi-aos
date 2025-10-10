package com.emotionstorage.daily_report.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.model.DailyReport
import com.emotionstorage.domain.model.DailyReport.EmotionLog
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDateTime
import javax.inject.Inject

data class DailyReportDetailState(
    val dailyReport: DailyReport? = null,
)

sealed class DailyReportDetailAction {
    data class Init(
        val id: String,
    ) : DailyReportDetailAction()
}

@HiltViewModel
class DailyReportDetailViewModel @Inject constructor() :
    ViewModel(),
    ContainerHost<DailyReportDetailState, Nothing> {
        override val container: Container<DailyReportDetailState, Nothing> = container(DailyReportDetailState())

        fun onAction(action: DailyReportDetailAction) {
            when (action) {
                is DailyReportDetailAction.Init -> {
                    handleInit(action.id)
                }
            }
        }

        private fun handleInit(id: String) =
            intent {
                // todo: call use case
                reduce {
                    state.copy(
                        dailyReport =
                            DailyReport(
                                id = id,
                                summaries =
                                    listOf(
                                        "아침에 출근길에 친구와 같이 출근하기로 했는데 친구가 지각해놓고 미안하단말을 하지 않아 기분이 좋지 않았어요.",
                                        "점심시간에는 동료와 업무 아이디어를 나누며 성취감을 느꼈고, 오후에는 팀 회의에서 의견이 무시당해 속상했어요.",
                                        "퇴근길에는 카페에서 혼자 시간을 보내며 마음을 다독였고, 집에서는 고양이와 시간을 보내며 차분해졌어요.",
                                    ),
                                keywords =
                                    listOf(
                                        "친구의 지각",
                                        "업무 아이디어",
                                        "회의 스트레스",
                                        "반려 동물",
                                        "회복시간",
                                    ),
                                emotionLogs =
                                    listOf(
                                        EmotionLog(
                                            emoji = "\uD83D\uDE20",
                                            label = "짜증남",
                                            description = "친구의 지각",
                                            time = LocalDateTime.now(),
                                        ),
                                        EmotionLog(
                                            emoji = "\uD83D\uDE42",
                                            label = "성취감",
                                            description = "업무 아이디어",
                                            time = LocalDateTime.now(),
                                        ),
                                        EmotionLog(
                                            emoji = "\uD83D\uDE1E",
                                            label = "서운함",
                                            description = "팀 회의에서 무시당함",
                                            time = LocalDateTime.now(),
                                        ),
                                        EmotionLog(
                                            emoji = "\uD83D\uDE0C",
                                            label = "안정감",
                                            description = "카페에서 힐링",
                                            time = LocalDateTime.now(),
                                        ),
                                        EmotionLog(
                                            emoji = "\uD83D\uDE3A",
                                            label = "따뜻함",
                                            description = "고양이와의 시간",
                                            time = LocalDateTime.now(),
                                        ),
                                    ),
                                createdAt = LocalDateTime.now(),
                                stressScore = 46,
                                happinessScore = 82,
                                emotionSummary = "하루종일 감정 기복은 있었지만, 하루의 끝은 안정감과 평온함으로 마무리 되었어요.",
                            ),
                    )
                }
            }
    }
