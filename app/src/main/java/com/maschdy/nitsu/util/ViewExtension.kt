package com.maschdy.nitsu.util

import android.view.View
import android.view.animation.Animation
import androidx.core.view.isVisible

fun View.startAnimation(
    animation: Animation,
    onFinish: () -> Unit = {},
    onStart: () -> Unit = {},
    onRepeat: () -> Unit = {}
) {
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) = onStart()
        override fun onAnimationRepeat(p0: Animation?) = onRepeat()
        override fun onAnimationEnd(p0: Animation?) = onFinish()
    })
    this.startAnimation(animation)
}

fun View.setVisibility(isVisible: Boolean) {
    this.isVisible = isVisible
}
