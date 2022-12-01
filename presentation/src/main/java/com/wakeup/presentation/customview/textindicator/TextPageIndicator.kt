package com.wakeup.presentation.customview.textindicator

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.viewpager2.widget.ViewPager2
import com.wakeup.presentation.R

class TextPageIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = android.R.attr.textViewStyle,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        getAttrs(attrs)
    }

    private fun getAttrs(attrs: AttributeSet) {
        val types = context.obtainStyledAttributes(attrs, R.styleable.TextPageIndicator)
        setTypedArray(types)
    }

    private fun setTypedArray(types: TypedArray) {
        val isDefaultSize =
            types.getBoolean(R.styleable.TextPageIndicator_defaultIndicatorStyle, false)
        if (isDefaultSize) setDefaultStyle()
    }

    private fun setDefaultStyle() {
        setBackgroundResource(R.drawable.bg_text_indicator)
        setTextColor(Color.parseColor(WHITE))
        gravity = Gravity.CENTER
        width = toDp(DEFAULT_WIDTH)
        height = toDp(DEFAULT_HEIGHT)
    }

    fun attachTo(viewPager: ViewPager2, lifecycleCoroutineScope: LifecycleCoroutineScope) {
        val textViewPagerCallback = TextViewPagerCallback(viewPager, this, lifecycleCoroutineScope)
        viewPager.registerOnPageChangeCallback(textViewPagerCallback)
    }

    private fun toDp(value: Int): Int {
        return Resources.getSystem().displayMetrics?.let { dm ->
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), dm)
        }?.toInt() ?: 0
    }

    private companion object {
        const val WHITE = "#FFFFFFFF"
        const val DEFAULT_WIDTH = 44
        const val DEFAULT_HEIGHT = 28
    }
}