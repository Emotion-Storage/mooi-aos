package com.emotionstorage.remote.request.timeCapsule

import kotlinx.serialization.Serializable

@Serializable
data class PatchTimeCapsuleNoteRequest (
    val content: String = ""
)
