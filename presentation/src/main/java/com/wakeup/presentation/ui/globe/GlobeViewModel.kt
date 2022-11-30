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
            GlobeModel(id = 0, name = "default"),
            GlobeModel(id = 1, name = "맛집"),
            GlobeModel(id = 0, name = "여행"),
            GlobeModel(id = 0, name = "경험"),
            GlobeModel(id = 0, name = "가족"),
            GlobeModel(id = 0, name = "1"),
            GlobeModel(id = 0, name = "2"),
            GlobeModel(id = 0, name = "3"),
        )
    )
    val globes = _globes.asStateFlow()


}