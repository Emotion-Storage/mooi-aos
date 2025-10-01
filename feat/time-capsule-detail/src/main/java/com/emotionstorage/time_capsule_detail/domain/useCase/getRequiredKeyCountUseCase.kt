package com.emotionstorage.time_capsule_detail.domain.useCase

import com.emotionstorage.common.getDaysBetween
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.absoluteValue

class getRequiredKeyCountUseCase @Inject constructor() {
    operator fun invoke(arriveAt: LocalDate): Int =
        when (LocalDate.now().getDaysBetween(arriveAt).absoluteValue) {
            in 0..7 -> 1
            in 8..30 -> 3
            in 31..90 -> 7
            in 90..180 -> 11
            in 180..365 -> 15
            else -> throw Throwable("invalid arriveAt")
        }
}
