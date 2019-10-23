package com.base.library.base

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.base.library.mvp.BPresenter
import com.base.library.mvp.BView
import com.base.library.util.getCacheObservable
import com.base.library.util.putCacheObservable
import com.base.library.util.roomInsertJournalRecord
import com.base.library.view.AlertDialog
import com.blankj.utilcode.util.LogUtils
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.functions.Consumer
import org.greenrobot.eventbus.EventBus
import talex.zsw.basecore.util.RegTool

/**
 * MVP Base Activity封装
 */
abstract class BActivity<T : BPresenter> : AppCompatActivity(), BView {

    abstract fun initArgs(intent: Intent?)
    abstract fun initContentView()
    abstract fun initData()

    var mPresenter: T? = null
    val mHandler: Handler by lazy { Handler() }
    val mApplication: BApplication by lazy { application as BApplication }
    private var alertDialog: AlertDialog? = null

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
        if (alertDialog != null && alertDialog!!.isShowing) {
            alertDialog?.setTitleText("正在加载数据")
            alertDialog?.changeAlertType(AlertDialog.PROGRESS_TYPE)
        } else {
            alertDialog = AlertDialog(this, AlertDialog.PROGRESS_TYPE).setTitleText("正在加载数据")
            alertDialog?.setCancelable(false)
            alertDialog?.show()
        }
    }

    override fun showDialog(
        alertType: Int,
        title: String?,
        content: String?,
        cancelText: String?,
        confirmText: String?,
        confirmListener: View.OnClickListener?,
        cancelListener: View.OnClickListener?
    ) {
        disDialog()
        try {
            if (alertDialog != null && alertDialog!!.isShowing) {
                alertDialog?.changeAlertType(alertType)
            } else {
                alertDialog = AlertDialog(this, alertType)
                alertDialog?.setCancelable(false)
            }
            alertDialog?.setTitleText(title)
            // content
            if (!RegTool.isNullString(content)) {
                alertDialog?.setContentText(content)
            } else {
                alertDialog?.showContentText(false)
            }
            // confirmText
            if (!RegTool.isNullString(confirmText)) {
                alertDialog?.setConfirmText(confirmText)
            }
            // cancelText
            if (!RegTool.isNullString(cancelText)) {
                alertDialog?.setCancelText(cancelText)
            } else {
                alertDialog?.showCancelButton(false)
            }
            alertDialog?.setConfirmClickListener(confirmListener)// confirmListener
            alertDialog?.setCancelClickListener(cancelListener)// confirmListener
            alertDialog?.show()
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
        alertDialog?.dismiss()
    }

    //关闭提示框之后销毁页面
    override fun disDialogFinsh() {
        alertDialog?.dismiss()
        finish()
    }

    //保存缓存
    override fun putCache(key: String, content: String, time: Int) {
        putCacheObservable(key, content, time)
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
            .subscribe { LogUtils.d(it) }
    }

    //获取缓存
    override fun getCache(key: String, consumer: Consumer<String>) {
        getCacheObservable(key)
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
            .subscribe(consumer)
    }

    override fun other(content: String, behavior: String, level: String) {
        roomInsertJournalRecord(content, behavior, level)
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
            .subscribe({
                //                LogUtils.d("插入的主键是:$it")
            }, {
                //                LogUtils.e("删除:$it.localizedMessage")
            })
    }

}