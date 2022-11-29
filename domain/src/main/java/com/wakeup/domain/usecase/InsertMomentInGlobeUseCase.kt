package com.wakeup.domain.usecase

import com.wakeup.domain.repository.RefRepository
import javax.inject.Inject

class InsertMomentInGlobeUseCase @Inject constructor(
    private val refRepository: RefRepository,
) {
    suspend fun invoke(momentId: Long, globeId: Long) {
        refRepository.saveMomentGlobeRef(momentId, globeId)
    }
}