package com.base.library.base

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.base.library.mvp.BPresenter
import com.base.library.mvp.BView
import com.base.library.util.roomInsertJournalRecord
import com.base.library.view.sweetdialog.SweetAlertDialog
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider

/**
 * MVP Base Fragment封装
 */
abstract class BMvpFragment<T : BPresenter> : Fragment(), BView {
    abstract fun initArgs(bundle: Bundle?)
    abstract fun initView(bundle: Bundle?)
    abstract fun initData()

    var mPresenter: T? = null
    private var sweetAlertDialog: SweetAlertDialog? = null
    private var mView: View? = null
    private var container: ViewGroup? = null
    private var inflater: LayoutInflater? = null

    val mHandler: Handler by lazy { Handler() }
    val mApplication: BApplication by lazy { activity?.application as BApplication }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.inflater = inflater
        this.container = container
        initArgs(arguments)
        initView(savedInstanceState)
        mPresenter?.let { lifecycle.addObserver(it) }
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    fun setContentView(layout: Int) {
        mView = inflater?.inflate(layout, container, false)
    }

    override fun onDestroyView() {
        disDialog()
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    override fun onDetach() {
        val childFragmentManager = Fragment::class.java.getDeclaredField("mChildFragmentManager")
        childFragmentManager.isAccessible = true
        childFragmentManager.set(this, null)
        super.onDetach()
    }

    override fun getContext() = activity!!

    override fun showDialog(loading: String?) {
        if (sweetAlertDialog != null && sweetAlertDialog!!.isShowing) {
            sweetAlertDialog?.setTitleText("正在加载数据")
            sweetAlertDialog?.changeAlertType(SweetAlertDialog.PROGRESS_TYPE)
        } else {
            sweetAlertDialog = SweetAlertDialog(
                context,
                SweetAlertDialog.PROGRESS_TYPE
            ).setTitleText("正在加载数据")
            sweetAlertDialog?.setCancelable(false)
            sweetAlertDialog?.show()
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
            if (sweetAlertDialog != null && sweetAlertDialog!!.isShowing) {
                sweetAlertDialog?.changeAlertType(alertType)
            } else {
                sweetAlertDialog = SweetAlertDialog(context, alertType)
                sweetAlertDialog?.setCancelable(false)
            }
            sweetAlertDialog?.setTitleText(title) // Title
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
        activity?.finish()
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