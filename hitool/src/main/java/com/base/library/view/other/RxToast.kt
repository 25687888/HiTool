package com.base.library.view.other

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.base.library.R
import com.blankj.utilcode.util.Utils

/**
 * 在系统的Toast基础上封装
 */

@SuppressLint("InflateParams")
object RxToast {

    @ColorInt
    private val DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF")

    @ColorInt
    private val ERROR_COLOR = Color.parseColor("#FD4C5B")

    @ColorInt
    private val INFO_COLOR = Color.parseColor("#3F51B5")

    @ColorInt
    private val SUCCESS_COLOR = Color.parseColor("#388E3C")

    @ColorInt
    private val WARNING_COLOR = Color.parseColor("#FFA900")

    private val TOAST_TYPEFACE = "sans-serif-condensed"

    private var currentToast: Toast?=null

    //*******************************************普通 使用ApplicationContext 方法*********************
    /**
     * Toast 替代方法 ：立即显示无需等待
     */
    private var mToast: Toast? = null
    private var mExitTime: Long = 0

    fun normal(message: String) {
        normal(Utils.getApp(), message, Toast.LENGTH_SHORT, null, false).show()
    }

    fun normal(message: String, icon: Drawable) {
        normal(Utils.getApp(), message, Toast.LENGTH_SHORT, icon, true).show()
    }

    fun normal(message: String, duration: Int) {
        normal(Utils.getApp(), message, duration, null, false).show()
    }

    fun normal(message: String, duration: Int, icon: Drawable) {
        normal(Utils.getApp(), message, duration, icon, true).show()
    }

    fun normal(message: String, duration: Int, icon: Drawable, withIcon: Boolean): Toast {
        return custom(Utils.getApp(), message, icon, DEFAULT_TEXT_COLOR, duration, withIcon)
    }

    fun warning(message: String) {
        warning(Utils.getApp(), message, Toast.LENGTH_SHORT, true).show()
    }

    fun warning(message: String, duration: Int) {
        warning(Utils.getApp(), message, duration, true).show()
    }

    fun warning(message: String, duration: Int, withIcon: Boolean): Toast {
        return custom(
            Utils.getApp(),
            message,
            getDrawable(Utils.getApp(), R.mipmap.ic_error_outline_white_48dp),
            DEFAULT_TEXT_COLOR,
            WARNING_COLOR,
            duration,
            withIcon,
            true
        )
    }

    fun info(message: String) {
        info(Utils.getApp(), message, Toast.LENGTH_SHORT, true).show()
    }

    fun info(message: String, duration: Int) {
        info(Utils.getApp(), message, duration, true).show()
    }

    fun info(message: String, duration: Int, withIcon: Boolean): Toast {
        return custom(
            Utils.getApp(),
            message,
            getDrawable(Utils.getApp(), R.mipmap.ic_info_outline_white_48dp),
            DEFAULT_TEXT_COLOR,
            INFO_COLOR,
            duration,
            withIcon,
            true
        )
    }

    fun success(message: String) {
        success(Utils.getApp(), message, Toast.LENGTH_SHORT, true).show()
    }

    fun success(message: String, duration: Int) {
        success(Utils.getApp(), message, duration, true).show()
    }

    fun success(message: String, duration: Int, withIcon: Boolean): Toast {
        return custom(
            Utils.getApp(),
            message,
            getDrawable(Utils.getApp(), R.mipmap.ic_check_white_48dp),
            DEFAULT_TEXT_COLOR,
            SUCCESS_COLOR,
            duration,
            withIcon,
            true
        )
    }

    fun error(message: String) {
        error(Utils.getApp(), message, Toast.LENGTH_SHORT, true).show()
    }
    //===========================================使用ApplicationContext 方法=========================

    //*******************************************常规方法********************************************

    fun error(message: String, duration: Int) {
        error(Utils.getApp(), message, duration, true).show()
    }

    fun error(message: String, duration: Int, withIcon: Boolean): Toast {
        return custom(
            Utils.getApp(),
            message,
            getDrawable(Utils.getApp(), R.mipmap.ic_clear_white_48dp),
            DEFAULT_TEXT_COLOR,
            ERROR_COLOR,
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    fun normal(context: Context, message: String): Toast {
        return normal(context, message, Toast.LENGTH_SHORT, null, false)
    }

    @CheckResult
    fun normal(context: Context, message: String, icon: Drawable): Toast {
        return normal(context, message, Toast.LENGTH_SHORT, icon, true)
    }

    @CheckResult
    fun normal(context: Context, message: String, duration: Int): Toast {
        return normal(context, message, duration, null, false)
    }

    @CheckResult
    @JvmOverloads
    fun normal(
        context: Context, message: String, duration: Int, icon: Drawable?, withIcon: Boolean = true
    ): Toast {
        return custom(context, message, icon, DEFAULT_TEXT_COLOR, duration, withIcon)
    }

    @CheckResult
    @JvmOverloads
    fun warning(
        context: Context,
        message: String,
        duration: Int = Toast.LENGTH_SHORT,
        withIcon: Boolean = true
    ): Toast {
        return custom(
            context,
            message,
            getDrawable(context, R.mipmap.ic_error_outline_white_48dp),
            DEFAULT_TEXT_COLOR,
            WARNING_COLOR,
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    @JvmOverloads
    fun info(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT, withIcon: Boolean = true): Toast {
        return custom(
            context,
            message,
            getDrawable(context, R.mipmap.ic_info_outline_white_48dp),
            DEFAULT_TEXT_COLOR,
            INFO_COLOR,
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    @JvmOverloads
    fun success(
        context: Context,
        message: String,
        duration: Int = Toast.LENGTH_SHORT,
        withIcon: Boolean = true
    ): Toast {
        return custom(
            context,
            message,
            getDrawable(context, R.mipmap.ic_check_white_48dp),
            DEFAULT_TEXT_COLOR,
            SUCCESS_COLOR,
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    @JvmOverloads
    fun error(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT, withIcon: Boolean = true): Toast {
        return custom(
            context,
            message,
            getDrawable(context, R.mipmap.ic_clear_white_48dp),
            DEFAULT_TEXT_COLOR,
            ERROR_COLOR,
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    fun custom(
        context: Context,
        message: String, icon: Drawable?, @ColorInt textColor: Int, duration: Int, withIcon: Boolean
    ): Toast {
        return custom(context, message, icon, textColor, -1, duration, withIcon, false)
    }

    //*******************************************内需方法********************************************

    @CheckResult
    fun custom(
        context: Context,
        message: String,
        @DrawableRes iconRes: Int,
        @ColorInt textColor: Int, @ColorInt tintColor: Int, duration: Int, withIcon: Boolean, shouldTint: Boolean
    ): Toast {
        return custom(
            context,
            message,
            getDrawable(context, iconRes),
            textColor,
            tintColor,
            duration,
            withIcon,
            shouldTint
        )
    }

    @CheckResult
    fun custom(
        context: Context,
        message: String, icon: Drawable?,
        @ColorInt textColor: Int, @ColorInt tintColor: Int, duration: Int, withIcon: Boolean, shouldTint: Boolean
    ): Toast {
        if (currentToast == null) currentToast = Toast(context)
        val toastLayout = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.toast_layout,
            null
        )
        val toastIcon = toastLayout.findViewById(R.id.toast_icon) as ImageView
        val toastTextView = toastLayout.findViewById(R.id.toast_text) as TextView
        val drawableFrame: Drawable?

        if (shouldTint) {
            drawableFrame = tint9PatchDrawableFrame(context, tintColor)
        } else {
            drawableFrame = getDrawable(context, R.mipmap.toast_frame)
        }
        setBackground(toastLayout, drawableFrame)

        if (withIcon) {
            if (icon == null) {
                throw IllegalArgumentException("Avoid passing 'icon' as null if 'withIcon' is set to true")
            }
            setBackground(toastIcon, icon)
        } else {
            toastIcon.visibility = View.GONE
        }

        toastTextView.setTextColor(textColor)
        toastTextView.text = message
        toastTextView.typeface = Typeface.create(TOAST_TYPEFACE, Typeface.NORMAL)

        currentToast?.view = toastLayout
        currentToast?.duration = duration
        return currentToast as Toast
    }

    fun tint9PatchDrawableFrame(context: Context, @ColorInt tintColor: Int): Drawable {
        val toastDrawable = getDrawable(context, R.mipmap.toast_frame) as NinePatchDrawable?
        toastDrawable!!.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        return toastDrawable
    }
    //===========================================内需方法============================================


    //******************************************系统 Toast 替代方法***************************************

    fun setBackground(view: View, drawable: Drawable?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = drawable
        } else {
            view.setBackgroundDrawable(drawable)
        }
    }

    fun getDrawable(context: Context, @DrawableRes id: Int): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getDrawable(id)
        } else {
            context.resources.getDrawable(id)
        }
    }

    /**
     * 封装了Toast的方法 :需要等待
     *
     * @param context Context
     * @param str     要显示的字符串
     * @param isLong  Toast.LENGTH_LONG / Toast.LENGTH_SHORT
     */
    fun showToast(context: Context, str: String, isLong: Boolean) {
        if (isLong) {
            Toast.makeText(context, str, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    fun showToastShort(str: String) {
        Toast.makeText(Utils.getApp(), str, Toast.LENGTH_SHORT).show()
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    fun showToastShort(resId: Int) {
        Toast.makeText(Utils.getApp(), Utils.getApp().getString(resId), Toast.LENGTH_SHORT).show()
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    fun showToastLong(str: String) {
        Toast.makeText(Utils.getApp(), str, Toast.LENGTH_LONG).show()
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    fun showToastLong(resId: Int) {
        Toast.makeText(Utils.getApp(), Utils.getApp().getString(resId), Toast.LENGTH_LONG).show()
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param msg 显示内容
     */
    fun showToast(msg: String) {
        if (mToast == null) {
            mToast = Toast.makeText(Utils.getApp(), msg, Toast.LENGTH_LONG)
        } else {
            mToast!!.setText(msg)
        }
        mToast!!.show()
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param resId String资源ID
     */
    fun showToast(resId: Int) {
        if (mToast == null) {
            mToast = Toast.makeText(Utils.getApp(), Utils.getApp().getString(resId), Toast.LENGTH_LONG)
        } else {
            mToast!!.setText(Utils.getApp().getString(resId))
        }
        mToast!!.show()
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param context  实体
     * @param resId    String资源ID
     * @param duration 显示时长
     */
    fun showToast(context: Context, resId: Int, duration: Int) {
        showToast(context, context.getString(resId), duration)
    }
    //===========================================Toast 替代方法======================================

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param context  实体
     * @param msg      要显示的字符串
     * @param duration 显示时长
     */
    fun showToast(context: Context, msg: String, duration: Int) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, duration)
        } else {
            mToast!!.setText(msg)
        }
        mToast!!.show()
    }

    fun doubleClickExit(): Boolean {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            RxToast.normal(Utils.getApp().resources.getString(R.string.tool_double_exit))
            mExitTime = System.currentTimeMillis()
            return false
        }
        return true
    }
}//===========================================常规方法============================================
