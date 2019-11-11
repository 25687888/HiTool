package com.base.library.view.sweetdialog

import android.content.Context
import com.base.library.R

class ProgressHelper(ctx: Context) {
    var progressWheel: ProgressWheel? = null
        set(progressWheel) {
            field = progressWheel
            updatePropsIfNeed()
        }
    var isSpinning: Boolean = false
        private set
    private var mSpinSpeed: Float = 0.toFloat()
    private var mBarWidth: Int = 0
    private var mBarColor: Int = 0
    private var mRimWidth: Int = 0
    private var mRimColor: Int = 0
    private var mIsInstantProgress: Boolean = false
    private var mProgressVal: Float = 0.toFloat()
    private var mCircleRadius: Int = 0

    var progress: Float
        get() = mProgressVal
        set(progress) {
            mIsInstantProgress = false
            mProgressVal = progress
            updatePropsIfNeed()
        }

    /**
     * @param circleRadius units using pixel
     */
    var circleRadius: Int
        get() = mCircleRadius
        set(circleRadius) {
            mCircleRadius = circleRadius
            updatePropsIfNeed()
        }

    var barWidth: Int
        get() = mBarWidth
        set(barWidth) {
            mBarWidth = barWidth
            updatePropsIfNeed()
        }

    var barColor: Int
        get() = mBarColor
        set(barColor) {
            mBarColor = barColor
            updatePropsIfNeed()
        }

    var rimWidth: Int
        get() = mRimWidth
        set(rimWidth) {
            mRimWidth = rimWidth
            updatePropsIfNeed()
        }

    var rimColor: Int
        get() = mRimColor
        set(rimColor) {
            mRimColor = rimColor
            updatePropsIfNeed()
        }

    var spinSpeed: Float
        get() = mSpinSpeed
        set(spinSpeed) {
            mSpinSpeed = spinSpeed
            updatePropsIfNeed()
        }

    init {
        isSpinning = true
        mSpinSpeed = 0.75f
        mBarWidth = ctx.resources.getDimensionPixelSize(R.dimen.common_circle_width) + 1
        mBarColor = ctx.resources.getColor(R.color.success_stroke_color)
        mRimWidth = 0
        mRimColor = 0x00000000
        mIsInstantProgress = false
        mProgressVal = -1f
        mCircleRadius = ctx.resources.getDimensionPixelOffset(R.dimen.progress_circle_radius)
    }

    private fun updatePropsIfNeed() {
        if (progressWheel != null) {
            if (!isSpinning && progressWheel!!.isSpinning) {
                progressWheel!!.stopSpinning()
            } else if (isSpinning && !progressWheel!!.isSpinning) {
                progressWheel!!.spin()
            }
            if (mSpinSpeed != progressWheel?.spinSpeed) {
                progressWheel!!.spinSpeed = mSpinSpeed
            }
            if (mBarWidth != progressWheel!!.barWidth) {
                progressWheel!!.barWidth = mBarWidth
            }
            if (mBarColor != progressWheel!!.barColor) {
                progressWheel!!.barColor = mBarColor
            }
            if (mRimWidth != progressWheel!!.rimWidth) {
                progressWheel!!.rimWidth = mRimWidth
            }
            if (mRimColor != progressWheel!!.rimColor) {
                progressWheel!!.rimColor = mRimColor
            }
            if (mProgressVal != progressWheel!!.progress) {
                if (mIsInstantProgress) {
                    progressWheel!!.setInstantProgress(mProgressVal)
                } else {
                    progressWheel!!.progress = mProgressVal
                }
            }
            if (mCircleRadius != progressWheel!!.circleRadius) {
                progressWheel!!.circleRadius = mCircleRadius
            }
        }
    }

    fun resetCount() {
        if (progressWheel != null) {
            progressWheel!!.resetCount()
        }
    }

    fun spin() {
        isSpinning = true
        updatePropsIfNeed()
    }

    fun stopSpinning() {
        isSpinning = false
        updatePropsIfNeed()
    }

    fun setInstantProgress(progress: Float) {
        mProgressVal = progress
        mIsInstantProgress = true
        updatePropsIfNeed()
    }
}
