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
) : ViewModel() {
    private val _globes = MutableStateFlow<List<GlobeModel>>(
        // mock
        listOf(
            GlobeModel(name = "default"),
            GlobeModel(name = "맛집"),
            GlobeModel(name = "여행"),
            GlobeModel(name = "경험"),
            GlobeModel(name = "가족"),
            GlobeModel(name = "1"),
            GlobeModel(name = "2"),
            GlobeModel(name = "3"),
        )
    )
    val globes = _globes.asStateFlow()


}