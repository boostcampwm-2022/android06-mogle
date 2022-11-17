package com.wakeup.domain.usecase

import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.MomentRepository
import javax.inject.Inject

class SaveMomentUseCase @Inject constructor(
    private val localRepository: MomentRepository
) {
    fun invoke(moment: Moment, location: Location, pictures: List<String>) {
        localRepository.saveMoment(moment, location, pictures)
    }
}