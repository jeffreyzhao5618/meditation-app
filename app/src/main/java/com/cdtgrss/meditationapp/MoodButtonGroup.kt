package com.cdtgrss.meditationapp

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout

class MoodButtonGroup(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs), View.OnClickListener {

    private var currentActiveButton: MoodButton? = null

    private val buttonIdArray = listOf(
        R.id.btn1,
        R.id.btn2,
        R.id.btn3,
        R.id.btn4,
        R.id.btn5
    )

    init {
        View.inflate(context, R.layout.view_mood_button_group, this)

        buttonIdArray.forEach { id ->
            findViewById<MoodButton>(id).setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {
        if (view != null && view::class == MoodButton::class) {
            currentActiveButton?.setBackgroundDrawable(solid = false)
            currentActiveButton = view as MoodButton
            view.setBackgroundDrawable(solid = true)
        }
    }
}