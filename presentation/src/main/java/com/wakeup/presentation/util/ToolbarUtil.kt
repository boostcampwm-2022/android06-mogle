package com.wakeup.presentation.util

import com.wakeup.presentation.databinding.ToolbarDefaultBinding


fun setToolbar(toolbar: ToolbarDefaultBinding, titleId: Int? = null, titleString: String? = null, onBackClick: () -> Unit) {
    if (titleId == null && titleString == null) {
        return
    }
    toolbar.tvTitle.text = if (titleId == null) titleString else toolbar.root.context.getString(titleId)
    toolbar.ivBackButton.setOnClickListener { onBackClick() }
}