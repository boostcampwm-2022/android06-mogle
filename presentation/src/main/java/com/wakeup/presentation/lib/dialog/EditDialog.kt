package com.wakeup.presentation.lib.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import com.wakeup.presentation.extension.showKeyboard


/**
 * @example (simple)
 *
 * EditDialog
 *  .with(requireContext(), R.id.dialog, R.id.editText)
 *  .setOnPositive(R.id.tv_positive) { Timber.d("OK") }
 *  .setOnNegative(R.id.tv_negative) { Timber.d("CANCEL") }
 *  .show()
 *
 */
class EditDialog private constructor(context: Context) :
    BaseDialog<EditDialog>(context) {

    private lateinit var editText: EditText

    companion object {
        private var INSTANCE: EditDialog? = null

        /**
         * @param context 화면에 띄울 컨텍스트를 지정
         * @param layoutId 원하는 dialog 레이아웃을 넣어준다.
         * @param editTextId dialog 레이아웃 내부의 editText Id를 넣어준다.
         */
        fun with(context: Context, layoutId: Int, editTextId: Int): EditDialog {
            val instance = INSTANCE
            if (instance?.context == context && instance.baseLayoutId == layoutId) {
                instance.editText.text.clear()
                return instance
            }

            INSTANCE = EditDialog(context).apply {
                builder = AlertDialog.Builder(context)
                dialogView = LayoutInflater.from(context).inflate(layoutId, null)
                dialog = builder.setView(dialogView).create()
                editText = dialogView.findViewById(editTextId)
                baseLayoutId = layoutId
            }

            return INSTANCE ?: throw IllegalStateException("Instance is null.")
        }
    }

    /**
     * dialog 가 나타날 때 키보드를 올린다.
     * @param boolean 키보드를 올릴 지 여부를 결정한다.
     */
    fun setKeyboardUp(boolean: Boolean): EditDialog {
        if (boolean.not()) return this
        dialog.setOnShowListener {
            context.showKeyboard(editText)
        }
        return this
    }


    /**
     * editText 의 text 값을 반환한다.
     */
    fun getTextInEditText(): String {
        return editText.text.toString()
    }
}