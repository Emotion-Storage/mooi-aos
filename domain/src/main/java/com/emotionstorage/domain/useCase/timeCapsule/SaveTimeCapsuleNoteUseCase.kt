package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.repo.TimeCapsuleRepository
import javax.inject.Inject

class SaveTimeCapsuleNoteUseCase @Inject constructor(
    private val timeCapsuleRepository: TimeCapsuleRepository
){
    suspend operator fun invoke(note: String) = timeCapsuleRepository.saveTimeCapsuleNote(note)
}
