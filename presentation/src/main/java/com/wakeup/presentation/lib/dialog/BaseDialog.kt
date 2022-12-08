package com.wakeup.presentation.lib.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView

abstract class BaseDialog<T : BaseDialog<T>>(protected val context: Context) {

    protected lateinit var builder: AlertDialog.Builder
    protected lateinit var dialog: AlertDialog
    protected lateinit var dialogView: View


    @Suppress("UNCHECKED_CAST")
    private fun self(): T = this as T


    /**
     * dialog 의 margin 값을 지정해준다.
     */
    fun setMargin(left: Int, top: Int, right: Int, bottom: Int): T {
        val inset = InsetDrawable(ColorDrawable(Color.TRANSPARENT), left, top, right, bottom)
        dialog.window?.setBackgroundDrawable(inset)
        return self()
    }

    /**
     * @param gravity dialog 의 위치를 지정해준다.
     */
    fun setGravity(gravity: Int = Gravity.CENTER): T {
        dialog.window?.setGravity(gravity)
        return self()
    }

    /**
     * @param boolean dialog 의 배경(외부)을 투명하게 할 지 여부를 결정한다.
     */
    fun setBackgroundTransParent(boolean: Boolean): T {
        if (boolean) {
            dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        return self()
    }

    /**
     * @param isCanceled dialog 외부를 터치하면 없이질 지 여부를 결정한다.
     */
    fun setCanceledOnTouchOutside(isCanceled: Boolean): T {
        dialog.setCanceledOnTouchOutside(isCanceled)
        return self()
    }

    /**
     * required
     *
     * @param resId dialog positive button 을 지정한다.
     * @param onPositive dialog 의 positive 버튼 클릭 이벤트를 지정해준다.
     *
     */
    fun setOnPositive(resId: Int, text: String, onPositive: (dialog: T) -> Unit): T {
        val positiveView = dialogView.findViewById<View>(resId)
        if (positiveView is TextView) { // Button is TextView's expand class
            positiveView.text = text
        }
        positiveView.setOnClickListener {
            onPositive(self())
            dialog.dismiss()
        }
        return self()
    }

    /**
     * required
     *
     * @param resId dialog negative button 을 지정한다.
     * @param onNegative dialog 의 negative 버튼 클릭 이벤트를 지정해준다.
     */
    fun setOnNegative(resId: Int, text: String, onNegative: (dialog: T) -> Unit): T {
        val negativeView = dialogView.findViewById<View>(resId)
        if (negativeView is TextView) { // Button is TextView's expand class
            negativeView.text = text
        }
        negativeView.setOnClickListener {
            onNegative(self())
            dialog.dismiss()
        }
        return self()
    }

    /**
     *
     * @param resId dialog 제목을 지정한다.
     * @param text dialog 제목의 text를 결정한다.
     */
    fun setTitle(resId: Int, text: String): T {
        dialogView.findViewById<TextView>(resId).text = text
        return self()
    }

    /**
     * required
     *
     * dialog 를 화면에 나타낸다.
     */
    fun show() {
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}