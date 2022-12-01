package com.wakeup.domain.usecase

import com.wakeup.domain.repository.RelationRepository
import javax.inject.Inject

class DeleteMomentInGlobeUseCase @Inject constructor(
    private val relationRepository: RelationRepository,
) {
    suspend fun invoke(momentId: Long, globeId: Long) {
        relationRepository.deleteMomentGlobeXRef(momentId, globeId)
    }
}