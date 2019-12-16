package com.base.library.view.sweetdialog

import android.app.Dialog
import android.content.Context
import android.view.View
import com.base.library.R

/**
 * 抽象加载框Dialog,作用：增加加载对话框的扩展性
 */
abstract class BSweetAlertDialog(context: Context,style: Int = R.style.alert_dialog) : Dialog(context, style) {
    abstract fun changeAlertType(progresS_TYPE: Int)
    abstract fun setTitleText(s: String?): BSweetAlertDialog
    abstract fun setContentText(content: String?): BSweetAlertDialog
    abstract fun showContentText(b: Boolean): BSweetAlertDialog
    abstract fun setConfirmText(confirmText: String?): BSweetAlertDialog
    abstract fun setCancelText(cancelText: String?): BSweetAlertDialog
    abstract fun showCancelButton(b: Boolean): BSweetAlertDialog
    abstract fun setConfirmClickListener(confirmListener: View.OnClickListener?): BSweetAlertDialog
    abstract fun setCancelClickListener(cancelListener: View.OnClickListener?): BSweetAlertDialog
    companion object {
        val NORMAL_TYPE = 0
        val ERROR_TYPE = 1
        val SUCCESS_TYPE = 2
        val WARNING_TYPE = 3
        val CUSTOM_IMAGE_TYPE = 4
        val PROGRESS_TYPE = 5
    }
}