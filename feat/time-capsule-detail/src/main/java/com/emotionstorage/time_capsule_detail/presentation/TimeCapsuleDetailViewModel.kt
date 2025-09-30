package com.emotionstorage.time_capsule_detail.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.model.TimeCapsule
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDateTime
import javax.inject.Inject

data class TimeCapsuleDatailState(
    val keyCount: Int = 0,
    val timeCapsule: TimeCapsule? = null,
    val isMindNoteChanged: Boolean = false,
)

sealed class TimeCapsuleDetailAction {
    data class Init(val id: String) : TimeCapsuleDetailAction()
    data class OnDeleteTimeCapsule(val id: String) : TimeCapsuleDetailAction()
}

sealed class TimeCapsuleDetailSideEffect {
    object DeleteTimeCapsuleSuccess : TimeCapsuleDetailSideEffect()
}

@HiltViewModel
class TimeCapsuleDetailViewModel @Inject constructor(
) : ViewModel(), ContainerHost<TimeCapsuleDatailState, TimeCapsuleDetailSideEffect> {
    override val container: Container<TimeCapsuleDatailState, TimeCapsuleDetailSideEffect> =
        container(TimeCapsuleDatailState())

    fun onAction(action: TimeCapsuleDetailAction) {
        when (action) {
            is TimeCapsuleDetailAction.Init -> {
                handleInit(action.id)
            }

            is TimeCapsuleDetailAction.OnDeleteTimeCapsule -> {
                handleDeleteTimeCapsule(action.id)
            }
        }
    }

    private fun handleInit(id: String) = intent {
        // todo: get key count
        // todo: get time capsule detail of id

        reduce {
            state.copy(
                keyCount = 5,
                timeCapsule = TimeCapsule(
                    id = "id",
                    status = TimeCapsule.STATUS.OPENED,
                    title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
                    summary =
                        "오늘 친구를 만났는데 친구가 지각해놓고 미안하단 말을 하지 않아서 집에 갈 때 기분이 좋지 않았어." +
                            "그렇지만 집에서 엄마가 해주신 맛있는 저녁을 먹고 기분이 좋아지더라. " +
                            "나를 가장 생각해주는 건 가족밖에 없다는 생각이 들었어.",
                    emotions =
                        listOf(
                            TimeCapsule.Emotion("서운함", icon = 0, 30.0f),
                            TimeCapsule.Emotion("고마움", icon = 3, 30.0f),
                            TimeCapsule.Emotion("안정감", icon = 4, 80.0f),
                        ),
                    comments =
                        listOf(
                            "오늘은 조금 힘든 일이 있었지만, 가족과의 따뜻한 시간 덕분에 긍정적인 감정으로 마무리했어요.",
                            "귀가 후 가족애와 안정감을 느끼면서, 부정적 감정을 회복할 수 있었어요.",
                            "감정이 복잡하게 얽힌 하루였네요. 하지만 작은 부분에서 감사함을 느끼는 모습이 멋져요.",
                        ),
                    note =
                        "아침엔 기분이 좀 꿀꿀했는데, 가족이랑 저녁 먹으면서 마음이 따뜻하게 풀려버렸다. " +
                            "사소한 일에 흔들렸지만 결국 웃으면서 하루를 마무리할 수 있어서 다행이야.",
                    logs = emptyList(),
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                ),
            )
        }
    }

    private fun handleDeleteTimeCapsule(id: String) = intent {
        // todo: delete time capsule
        postSideEffect(TimeCapsuleDetailSideEffect.DeleteTimeCapsuleSuccess)
    }
}
