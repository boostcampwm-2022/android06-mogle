package com.wakeup.presentation.util

import com.wakeup.presentation.databinding.ToolbarDefaultBinding


fun setToolbar(toolbar: ToolbarDefaultBinding, titleId: Int, onBackClick: () -> Unit) {
    toolbar.tvTitle.text = toolbar.root.context.getString(titleId)
    toolbar.tvIcon.setOnClickListener { onBackClick() }
}