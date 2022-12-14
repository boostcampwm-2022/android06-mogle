package com.wakeup.presentation.lib.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater

/**
 * @example (simple)
 *
 * NormalDialog.with(requireContext(), R.id.dialog)
 *  .setOnPositive(R.id.tv_positive) { Timber.d("OK") }
 *  .setOnNegative(R.id.tv_negative) { Timber.d("CANCEL") }
 *  .show()
 *
 */
class NormalDialog private constructor(context: Context) :
    BaseDialog<NormalDialog>(context) {

    companion object {
        private var INSTANCE: NormalDialog? = null

        /**
         * @param context 화면에 띄울 컨텍스트를 지정
         * @param layoutId 원하는 dialog 레이아웃을 넣어준다.
         */
        fun with(context: Context, layoutId: Int): NormalDialog {
            val instance = INSTANCE
            if (instance?.context == context && instance.baseLayoutId == layoutId) return instance

            INSTANCE = NormalDialog(context).apply {
                builder = AlertDialog.Builder(context)
                dialogView = LayoutInflater.from(context).inflate(layoutId, null)
                dialog = builder.setView(dialogView).create()
                baseLayoutId = layoutId
            }

            return INSTANCE ?: throw IllegalStateException("Instance is null.")
        }
    }
}