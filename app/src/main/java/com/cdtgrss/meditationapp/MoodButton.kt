package com.cdtgrss.meditationapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class MoodButton(context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatButton(context, attrs) {
    private var mRegularDrawable: Drawable
    private var mSolidDrawable: Drawable

    init {
        context.theme
            .obtainStyledAttributes(attrs, R.styleable.MoodButton, 0, 0).apply {
                try {
                    mRegularDrawable = getDrawable(R.styleable.MoodButton_iconRegularDrawable)!!
                    mSolidDrawable = getDrawable(R.styleable.MoodButton_iconSolidDrawable)!!
                } finally {
                    recycle()
                }
            }

    }

    fun setBackgroundDrawable(solid: Boolean = false) {
        background = if (solid) mSolidDrawable else mRegularDrawable
    }

}