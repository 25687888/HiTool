package com.base.library.base

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.base.library.mvp.BPresenter
import com.base.library.mvp.BView
import com.base.library.view.sweetdialog.BSweetAlertDialog
import com.base.library.view.sweetdialog.SweetAlertDialog
import org.greenrobot.eventbus.EventBus

/**
 * MVP Base Activity封装
 */
abstract class BMvpActivity<T : BPresenter> : AppCompatActivity(), BView {

    abstract fun initArgs(intent: Intent?)
    abstract fun initContentView()
    abstract fun initData()
    abstract fun getSweetAlertDialog(): BSweetAlertDialog?

    var mPresenter: T? = null
    val mHandler: Handler by lazy { Handler() }
    val mApplication: BApplication by lazy { application as BApplication }
    private var sweetAlertDialog: BSweetAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs(intent)
        initContentView()
        mPresenter?.let { lifecycle.addObserver(it) }
        window.decorView.post { mHandler.post { initData() } }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onDestroy() {
        disDialog()
        mHandler?.removeCallbacksAndMessages(null)
        var eventBus = EventBus.getDefault()
        if (eventBus.isRegistered(this)) eventBus.unregister(this)
        super.onDestroy()
    }

    override fun getContext() = this

    override fun showDialog(loading: String?) {
        if (sweetAlertDialog != null && sweetAlertDialog!!.isShowing) {
            sweetAlertDialog?.setTitleText(loading)
            sweetAlertDialog?.showContentText(false)
            sweetAlertDialog?.changeAlertType(BSweetAlertDialog.PROGRESS_TYPE)
        } else {
            if (getSweetAlertDialog() != null) {
                sweetAlertDialog = getSweetAlertDialog()
            } else {
                sweetAlertDialog = SweetAlertDialog(this, BSweetAlertDialog.PROGRESS_TYPE)
            }
            sweetAlertDialog?.changeAlertType(BSweetAlertDialog.PROGRESS_TYPE)
            sweetAlertDialog?.setTitleText(loading)
            sweetAlertDialog?.showContentText(false)
            sweetAlertDialog?.show()
        }
    }

    override fun showDialog(
        alertType: Int,
        title: String?,
        content: String?,
        confirmText: String?,
        cancelText: String?,
        confirmListener: View.OnClickListener?,
        cancelListener: View.OnClickListener?
    ) {
        disDialog()
        try {
            if (sweetAlertDialog != null && sweetAlertDialog!!.isShowing) {
                sweetAlertDialog?.changeAlertType(alertType)
            } else {
                if (getSweetAlertDialog() != null) {
                    sweetAlertDialog = getSweetAlertDialog()
                } else {
                    sweetAlertDialog = SweetAlertDialog(this, alertType)
                }
                sweetAlertDialog?.changeAlertType(alertType)
                sweetAlertDialog?.setCancelable(false)
            }
            sweetAlertDialog?.setTitleText(title)
            // content
            if (!TextUtils.isEmpty(content)) {
                sweetAlertDialog?.setContentText(content)
            } else {
                sweetAlertDialog?.showContentText(false)
            }
            // confirmText
            if (!TextUtils.isEmpty(confirmText)) {
                sweetAlertDialog?.setConfirmText(confirmText)
            }
            // cancelText
            if (!TextUtils.isEmpty(cancelText)) {
                sweetAlertDialog?.setCancelText(cancelText)
            } else {
                sweetAlertDialog?.showCancelButton(false)
            }
            sweetAlertDialog?.setConfirmClickListener(confirmListener)// confirmListener
            sweetAlertDialog?.setCancelClickListener(cancelListener)// confirmListener
            sweetAlertDialog?.show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    //确定 - 关闭提示框
    override fun getConfirmDisListener() = View.OnClickListener { disDialog() }

    //确定 - 关闭页面
    override fun getConfirmFinishListener() = View.OnClickListener { disDialogFinsh() }

    //取消 - 关闭提示框
    override fun getCancelDisListener() = View.OnClickListener { disDialog() }

    //取消 - 关闭页面
    override fun getCancelFinishListener() = View.OnClickListener { disDialogFinsh() }

    //关闭提示框
    override fun disDialog() {
        sweetAlertDialog?.dismiss()
    }

    //关闭提示框之后销毁页面
    override fun disDialogFinsh() {
        sweetAlertDialog?.dismiss()
        finish()
    }
}