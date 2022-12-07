package com.wakeup.domain.usecase.globe

import com.wakeup.domain.repository.RelationRepository
import javax.inject.Inject

class InsertMomentInGlobeUseCase @Inject constructor(
    private val relationRepository: RelationRepository,
) {
    suspend fun invoke(momentId: Long, globeId: Long) {
        relationRepository.saveMomentGlobeXRef(momentId, globeId)
    }
}