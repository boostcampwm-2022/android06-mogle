package com.wakeup.presentation.ui.globe

import androidx.lifecycle.ViewModel
import com.wakeup.presentation.model.GlobeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GlobeViewModel @Inject constructor(
    // todo add UseCase 'getGlobes'
): ViewModel() {
    private val _globes = MutableStateFlow<List<GlobeModel>>(
        // mock
        listOf(
            GlobeModel(0, "default"),
            GlobeModel(1, "맛집"),
            GlobeModel(2, "여행"),
            GlobeModel(3, "경험"),
            GlobeModel(4, "가족"),
            GlobeModel(5, "1"),
            GlobeModel(6, "2"),
            GlobeModel(7, "3"),
        )
    )
    val globes = _globes.asStateFlow()


}