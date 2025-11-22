package com.emotionstorage.remote.modelMapper

import com.emotionstorage.remote.request.timeCapsule.PatchTimeCapsuleFavoriteRequest
import com.emotionstorage.remote.request.timeCapsule.PatchTimeCapsuleNoteRequest
import com.emotionstorage.remote.request.timeCapsule.PostTimeCapsuleOpenAtRequest
import java.time.LocalDateTime

internal fun LocalDateTime.toPostOpenAtRequest(id: Long) =
    PostTimeCapsuleOpenAtRequest(
        capsuleId = id,
        storedAt = LocalDateTime.now().toString(),
        openAt = this.toString(),
    )

internal fun String.toPatchNoteRequest() =
    PatchTimeCapsuleNoteRequest(
        content = this,
    )

internal fun Boolean.toPatchFavoriteRequest() =
    PatchTimeCapsuleFavoriteRequest(
        addFavorite = this,
    )
