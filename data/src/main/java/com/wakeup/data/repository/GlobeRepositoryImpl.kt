package com.wakeup.data.repository

import com.wakeup.domain.model.Globe
import com.wakeup.domain.repository.GlobeRepository
import javax.inject.Inject

class GlobeRepositoryImpl  @Inject constructor(
    private val localRepository: GlobeRepository,
) : GlobeRepository {

    override suspend fun createGlobe(globe: Globe) {
        localRepository.createGlobe(globe)
    }

    override suspend fun updateGlobe(globe: Globe) {
        localRepository.updateGlobe(globe)
    }

    override suspend fun deleteGlobe(globe: Globe) {
        localRepository.deleteGlobe(globe)
    }

    override suspend fun getGlobes(): List<Globe> {
        return localRepository.getGlobes()
    }
}
