package com.base.library.mvp

import android.content.Context
import android.view.View
import com.base.library.view.AlertDialog
import io.reactivex.functions.Consumer

/**
 * 作用: 基于MVP架构的View 视图基类
 */
interface BView {

    fun showDialog(loading: String? = "正在加载...")

    fun showDialog(
        alertType: Int = AlertDialog.CUSTOM_IMAGE_TYPE,
        title: String? = "提示",
        content: String? = "",
        cancelBtnText: String? = "取消",
        confirmBtnText: String? = "确定",
        confirmListener: View.OnClickListener? = null, // 确定按钮回调
        cancelListener: View.OnClickListener? = null // 取消按钮回调
    )

    fun getConfirmDisListener(): View.OnClickListener
    fun getConfirmFinishListener(): View.OnClickListener

    fun getCancelDisListener(): View.OnClickListener
    fun getCancelFinishListener(): View.OnClickListener

    fun disDialog() // 销毁对话框

    fun disDialogFinsh() // 销毁对话框 之后 关闭页面

    /**
     * Activity获取当前this，Fragment获取getActivity
     */
    fun getContext(): Context

    /**
     * 添加 获取 缓存
     */
    fun putCache(key: String, content: String, time: Int = -1)

    fun getCache(key: String, consumer: Consumer<String>)

    /**
     * 可以用来保存日志
     */
    fun other(content: String, behavior: String, level: String)

}
