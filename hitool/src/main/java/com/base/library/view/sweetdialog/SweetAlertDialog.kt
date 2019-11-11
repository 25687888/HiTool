package com.base.library.view.sweetdialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.base.library.R

class SweetAlertDialog @JvmOverloads constructor(context: Context, alertType: Int = NORMAL_TYPE) :
    Dialog(context, R.style.alert_dialog), View.OnClickListener {
    private var mDialogView: View? = null
    private val mModalInAnim: AnimationSet
    private val mModalOutAnim: AnimationSet
    private val mOverlayOutAnim: Animation
    private var mErrorInAnim: Animation? = null
    private val mErrorXInAnim: AnimationSet
    private val mSuccessLayoutAnimSet: AnimationSet
    private var mSuccessBowAnim: Animation? = null
    private var mTitleTextView: TextView? = null
    private var mContentTextView: TextView? = null
    private var mTitleText: String? = null
    private var mContentText: String? = null
    var isShowCancelButton: Boolean = false
    var isShowContentText: Boolean = false
    private var mCancelText: String? = null
    private var mConfirmText: String? = null
    var alerType: Int = 0
    private var mErrorFrame: FrameLayout? = null
    private var mSuccessFrame: FrameLayout? = null
    private var mProgressFrame: FrameLayout? = null
    private var mSuccessTick: SuccessTickView? = null
    private var mErrorX: ImageView? = null
    private var mSuccessLeftMask: View? = null
    private var mSuccessRightMask: View? = null
    private var mCustomImgDrawable: Drawable? = null
    private var mCustomImage: ImageView? = null
    private var mConfirmButton: Button? = null
    private var mCancelButton: Button? = null
    val progressHelper: ProgressHelper
    private var mWarningFrame: FrameLayout? = null
    private var mCancelClickListener: View.OnClickListener? = null
    private var mConfirmClickListener: View.OnClickListener? = null
    private var mCloseFromCancel: Boolean = false

    init {
        setCancelable(true)
        setCanceledOnTouchOutside(false)
        progressHelper = ProgressHelper(context)
        alerType = alertType
        mErrorInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.sweetalert_error_frame_in)
        mErrorXInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.sweetalert_error_x_in) as AnimationSet

        // 2.3.x system don't support alpha-animation on layer-list drawable
        // remove it from animation set
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            val childAnims = mErrorXInAnim.animations
            var idx = 0
            while (idx < childAnims.size) {
                if (childAnims[idx] is AlphaAnimation) {
                    break
                }
                idx++
            }
            if (idx < childAnims.size) {
                childAnims.removeAt(idx)
            }
        }
        mSuccessBowAnim = AnimationUtils.loadAnimation(getContext(), R.anim.success_bow_roate)
        mSuccessLayoutAnimSet = AnimationUtils.loadAnimation(getContext(), R.anim.success_mask_layout) as AnimationSet
        mModalInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.modal_in) as AnimationSet
        mModalOutAnim = AnimationUtils.loadAnimation(getContext(), R.anim.modal_out) as AnimationSet
        mModalOutAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                mDialogView!!.visibility = View.GONE
                mDialogView!!.post {
                    if (mCloseFromCancel) {
                        super@SweetAlertDialog.cancel()
                    } else {
                        super@SweetAlertDialog.dismiss()
                    }
                }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        // dialog overlay fade out
        mOverlayOutAnim = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                val wlp = window!!.attributes
                wlp.alpha = 1 - interpolatedTime

                //设置 dialog  居中显示
                window!!.attributes = wlp
            }
        }
        mOverlayOutAnim.setDuration(120)

        window!!.setGravity(Gravity.CENTER)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sweet_alert_dialog)
        mDialogView = window!!.decorView.findViewById(android.R.id.content)
        mTitleTextView = findViewById<View>(R.id.title_text) as TextView
        mContentTextView = findViewById<View>(R.id.content_text) as TextView
        mErrorFrame = findViewById<View>(R.id.error_frame) as FrameLayout
        mErrorX = mErrorFrame!!.findViewById<View>(R.id.error_x) as ImageView
        mSuccessFrame = findViewById<View>(R.id.success_frame) as FrameLayout
        mProgressFrame = findViewById<View>(R.id.progress_dialog) as FrameLayout
        mSuccessTick = mSuccessFrame!!.findViewById<View>(R.id.success_tick) as SuccessTickView
        mSuccessLeftMask = mSuccessFrame!!.findViewById(R.id.mask_left)
        mSuccessRightMask = mSuccessFrame!!.findViewById(R.id.mask_right)
        mCustomImage = findViewById<View>(R.id.custom_image) as ImageView
        mWarningFrame = findViewById<View>(R.id.warning_frame) as FrameLayout
        mConfirmButton = findViewById<View>(R.id.confirm_button) as Button
        mCancelButton = findViewById<View>(R.id.cancel_button) as Button
        progressHelper.progressWheel = findViewById<View>(R.id.progressWheel) as ProgressWheel
        mConfirmButton!!.setOnClickListener(this)
        mCancelButton!!.setOnClickListener(this)
        setTitleText(mTitleText)
        setContentText(mContentText)
        setCancelText(mCancelText)
        setConfirmText(mConfirmText)
        changeAlertType(alerType, true)
    }

    private fun restore() {
        mCustomImage!!.visibility = View.GONE
        mErrorFrame!!.visibility = View.GONE
        mSuccessFrame!!.visibility = View.GONE
        mWarningFrame!!.visibility = View.GONE
        mProgressFrame!!.visibility = View.GONE
        mConfirmButton!!.visibility = View.VISIBLE

        mConfirmButton!!.setBackgroundResource(R.drawable.sweetalert_blue_button_background)
        //        mConfirmButton.setBackgroundResource(R.drawable.blue_button_background); //这个是默认的
        mErrorFrame!!.clearAnimation()
        mErrorX!!.clearAnimation()
        mSuccessTick!!.clearAnimation()
        mSuccessLeftMask!!.clearAnimation()
        mSuccessRightMask!!.clearAnimation()
    }

    private fun playAnimation() {
        if (alerType == ERROR_TYPE) {
            mErrorFrame!!.startAnimation(mErrorInAnim)
            mErrorX!!.startAnimation(mErrorXInAnim)
        } else if (alerType == SUCCESS_TYPE) {
            mSuccessTick!!.startTickAnim()
            mSuccessRightMask!!.startAnimation(mSuccessBowAnim)
        }
    }

    private fun changeAlertType(alertType: Int, fromCreate: Boolean) {
        alerType = alertType
        // call after created views
        if (mDialogView != null) {
            if (!fromCreate) {
                // restore all of views state before switching alert type
                restore()
            }
            when (alerType) {
                ERROR_TYPE -> mErrorFrame!!.visibility = View.VISIBLE
                SUCCESS_TYPE -> {
                    mSuccessFrame!!.visibility = View.VISIBLE
                    // initial rotate layout of success mask
                    mSuccessLeftMask!!.startAnimation(mSuccessLayoutAnimSet.animations[0])
                    mSuccessRightMask!!.startAnimation(mSuccessLayoutAnimSet.animations[1])
                }
                WARNING_TYPE -> {
                    mConfirmButton!!.setBackgroundResource(R.drawable.sweetalert_red_button_background) // 自己改写的确定按钮背景
                    //                    mConfirmButton.setBackgroundResource(R.drawable.red_button_background); // 原生的确定按钮点击背景
                    mWarningFrame!!.visibility = View.VISIBLE
                }
                CUSTOM_IMAGE_TYPE -> setCustomImage(mCustomImgDrawable)
                PROGRESS_TYPE -> {
                    mProgressFrame!!.visibility = View.VISIBLE
                    mConfirmButton!!.visibility = View.GONE
                }
            }
            if (!fromCreate) {
                playAnimation()
            }
        }
    }

    fun changeAlertType(alertType: Int) {
        changeAlertType(alertType, false)
    }


    fun getTitleText(): String? {
        return mTitleText
    }

    fun setTitleText(text: String?): SweetAlertDialog {
        mTitleText = text
        if (mTitleTextView != null && mTitleText != null) {
            mTitleTextView!!.text = mTitleText
        }
        return this
    }

    fun setCustomImage(drawable: Drawable?): SweetAlertDialog {
        mCustomImgDrawable = drawable
        if (mCustomImage != null && mCustomImgDrawable != null) {
            mCustomImage!!.visibility = View.VISIBLE
            mCustomImage!!.setImageDrawable(mCustomImgDrawable)
        }
        return this
    }

    fun setCustomImage(resourceId: Int): SweetAlertDialog {
        return setCustomImage(context.resources.getDrawable(resourceId))
    }

    fun getContentText(): String? {
        return mContentText
    }

    fun setContentText(text: String?): SweetAlertDialog {
        mContentText = text
        if (mContentTextView != null && mContentText != null) {
            showContentText(true)
            mContentTextView!!.text = mContentText
        }
        return this
    }

    fun showCancelButton(isShow: Boolean): SweetAlertDialog {
        isShowCancelButton = isShow
        if (mCancelButton != null) {
            mCancelButton!!.visibility = if (isShowCancelButton) View.VISIBLE else View.GONE
        }
        return this
    }

    fun showContentText(isShow: Boolean): SweetAlertDialog {
        isShowContentText = isShow
        if (mContentTextView != null) {
            mContentTextView!!.visibility = if (isShowContentText) View.VISIBLE else View.GONE
        }
        return this
    }

    fun getCancelText(): String? {
        return mCancelText
    }

    fun setCancelText(text: String?): SweetAlertDialog {
        mCancelText = text
        if (mCancelButton != null && mCancelText != null) {
            showCancelButton(true)
            mCancelButton!!.text = mCancelText
        }
        return this
    }

    fun getConfirmText(): String? {
        return mConfirmText
    }

    fun setConfirmText(text: String?): SweetAlertDialog {
        mConfirmText = text
        if (mConfirmButton != null && mConfirmText != null) {
            mConfirmButton!!.text = mConfirmText
        }
        return this
    }

    fun setCancelClickListener(listener: View.OnClickListener?): SweetAlertDialog {
        mCancelClickListener = listener
        return this
    }

    fun setConfirmClickListener(listener: View.OnClickListener?): SweetAlertDialog {
        mConfirmClickListener = listener
        return this
    }

    override fun onStart() {
        mDialogView!!.startAnimation(mModalInAnim)
        playAnimation()
    }

    /**
     * The real Dialog.cancel() will be invoked async-ly after the animation finishes.
     */
    override fun cancel() {
        dismissWithAnimation(true)
    }

    /**
     * The real Dialog.dismiss() will be invoked async-ly after the animation finishes.
     */
    fun dismissWithAnimation() {
        dismissWithAnimation(false)
    }

    private fun dismissWithAnimation(fromCancel: Boolean) {
        mCloseFromCancel = fromCancel
        mConfirmButton!!.startAnimation(mOverlayOutAnim)
        mDialogView!!.startAnimation(mModalOutAnim)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.cancel_button) {
            if (mCancelClickListener != null) {
                mCancelClickListener?.onClick(v)
            } else {
                dismissWithAnimation()
            }
        } else if (v.id == R.id.confirm_button) {
            if (mConfirmClickListener != null) {
                mConfirmClickListener?.onClick(v)
            } else {
                dismissWithAnimation()
            }
        }
    }

    companion object {

        val NORMAL_TYPE = 0
        val ERROR_TYPE = 1
        val SUCCESS_TYPE = 2
        val WARNING_TYPE = 3
        val CUSTOM_IMAGE_TYPE = 4
        val PROGRESS_TYPE = 5
    }
}