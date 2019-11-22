package com.sendinfo.tool.module

import android.view.View
import android.widget.ProgressBar
import com.base.library.mvp.BPresenter
import com.base.library.mvp.BasePresenter
import com.base.library.mvp.BaseView
import com.base.library.util.WebViewTool
import com.sendinfo.tool.base.BActivity
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.b_titlebar.*
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.PermissionUtils
import com.sendinfo.tool.R
import com.sendinfo.tool.tools.getWebUrl
import com.sendinfo.tool.tools.putWebUrl
import com.sendinfo.tool.views.dialogs.InputDialog


class WebMvpActivity : BActivity<BPresenter>(), BaseView {

    override fun bindPresenter(): BPresenter = BasePresenter(this)

    override fun setContentView(): Int = R.layout.activity_web

    override fun initView() {
        bTitlebar?.visibility = View.GONE
        pingNet()
        PermissionUtils
            .permission(
                PermissionConstants.STORAGE,
                PermissionConstants.MICROPHONE,
                PermissionConstants.CAMERA,
                PermissionConstants.LOCATION
            )
            .callback(object : PermissionUtils.FullCallback {
                override fun onGranted(permissionsGranted: List<String>) {
                    LogUtils.d(permissionsGranted)
                }

                override fun onDenied(permissionsDeniedForever: List<String>, permissionsDenied: List<String>) {
                    LogUtils.d(permissionsDeniedForever, permissionsDenied)
                }
            })
            .request()
    }

    override fun initData() {
        super.initData()
        viewChange.setOnClickListener { showInputDialog() }
        webView.setOnLongClickListener { true }
        WebViewTool.setWebData(getWebUrl(), webView, null)
    }

    override fun bindData(bodyStr: String) {
    }

    override fun bindError(string: String) {
    }

    private var inputDialog: InputDialog? = null
    private fun showInputDialog() {
        inputDialog?.dismiss()
        inputDialog = InputDialog(this).setTitleText("网页链接").setContentText(getWebUrl())
        inputDialog?.setConfirmClickListener(View.OnClickListener {
            inputDialog?.dismiss()
            putWebUrl(inputDialog?.getContentText() ?: "")
            WebViewTool.setWebData(getWebUrl(), webView, ProgressBar(this))
        })
        inputDialog?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        inputDialog?.dismiss()
    }

    private var netIsOK = false
    private fun pingNet() {
        mHandler.postDelayed({
            NetworkUtils.isAvailableAsync {
                netIsOK = it
                if (!netIsOK) pingNet()
                else webView.reload()
            }
        }, 2000)
    }
}
