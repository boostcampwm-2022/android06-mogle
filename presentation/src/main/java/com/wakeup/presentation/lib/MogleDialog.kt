package com.wakeup.presentation.lib

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.wakeup.presentation.databinding.DialogPlaceBinding
import com.wakeup.presentation.model.PlaceModel

/**
 *
 * @example
 *
 * MogleDialog.with(requireContext())
 *  .setView(R.layout.dialog_place)
 *  .setOnPositive(R.id.tv_positive) { Timber.d("OK") }
 *  .setOnNegative(R.id.tv_negative) { Timber.d("CANCEL") }
 *  .show()
 *
 */
class MogleDialog private constructor() {

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private lateinit var dialogView: View


    companion object {

        /**
         *
         * @param context 화면에 띄울 컨텍스트를 지정
         * @param layoutId 원하는 dialog 레이아웃을 넣어준다.
         *
         */

        fun with(context: Context, layoutId: Int): MogleDialog {
            return MogleDialog().apply {
                builder = AlertDialog.Builder(context)
                dialogView = LayoutInflater.from(context).inflate(layoutId, null)
                dialog = builder.setView(dialogView).create()
            }
        }
    }


    /**
     * dialog의 margin 값을 지정해준다.
     */
    fun setMargin(left: Int, top: Int, right: Int, bottom: Int): MogleDialog {
        val inset = InsetDrawable(ColorDrawable(Color.TRANSPARENT), left, top, right, bottom)
        dialog.window?.setBackgroundDrawable(inset)
        return this
    }

    /**
     * @param gravity dialog의 위치를 지정해준다.
     */
    fun setGravity(gravity: Int = Gravity.CENTER): MogleDialog {
        dialog.window?.setGravity(gravity)
        return this
    }

    /**
     * @param boolean dialog의 배경(외부)을 투명하게 할 지 여부를 결정한다.
     */
    fun setBackgroundTransParent(boolean: Boolean): MogleDialog {
        if (boolean) {
            dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        return this
    }

    /**
     * @param isCanceled dialog 외부를 터치하면 없이질 지 여부를 결정한다.
     *
     */
    fun setCanceledOnTouchOutside(isCanceled: Boolean): MogleDialog {
        dialog.setCanceledOnTouchOutside(isCanceled)
        return this
    }

    /**
     *
     * required
     *
     * @param resId dialog positive button 을 지정한다.
     * @param onPositive dialog의 positive 버튼 클릭 이벤트를 지정해준다.
     *
     */
    fun setOnPositive(resId: Int, onPositive: () -> Unit): MogleDialog = apply {
        dialogView.findViewById<View>(resId).setOnClickListener {
            onPositive()
            dialog.dismiss()
        }
    }

    /**
     * required
     *
     * @param resId dialog negative button 을 지정한다.
     * @param onNegative dialog의 negative 버튼 클릭 이벤트를 지정해준다.
     */
    fun setOnNegative(resId: Int, onNegative: () -> Unit): MogleDialog = apply {
        dialogView.findViewById<View>(resId).setOnClickListener {
            onNegative()
            dialog.dismiss()
        }
    }

    /**
     * required
     *
     * dialog 를 화면에 나타낸다.
     */
    fun show() {
        dialog.show()
    }


    /**
     * placeSearchDialog 를 위한겁니다.
     */
    fun setPlace(place: PlaceModel): MogleDialog {
        val binding = DialogPlaceBinding.bind(dialogView)
        binding.place = place
        return this
    }
}