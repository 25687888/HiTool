package com.sendinfo.tool.module

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.view.View
import androidx.core.content.ContextCompat
import com.base.library.mvp.BPresenter
import com.base.library.mvp.BasePresenter
import com.base.library.mvp.BaseView
import com.base.library.util.setThreeClick
import com.base.library.util.webview.WebViewTool
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
import com.blankj.utilcode.util.PermissionUtils.FullCallback
import com.sendinfo.tool.tools.JsBridge
import com.sendinfo.tool.tools.LocationTool

class WebMvpActivity : BActivity<BPresenter>(), BaseView {
    private var jsBridge = JsBridge()
    private val locationTool: LocationTool by lazy { LocationTool(getContext()).apply { lifecycle.addObserver(this) } }

    override fun bindPresenter(): BPresenter = BasePresenter(this)

    override fun setContentView(): Int = R.layout.activity_web

    override fun initView() {
        bTitlebar?.visibility = View.GONE
        pingNet()
        PermissionUtils.permission(
            PermissionConstants.STORAGE,
            PermissionConstants.MICROPHONE,
            PermissionConstants.CAMERA,
            PermissionConstants.LOCATION
        ).callback(object : FullCallback {
            override fun onGranted(permissionsGranted: List<String>) {
                if (permissionsGranted.contains(ACCESS_COARSE_LOCATION) || permissionsGranted.contains(
                        ACCESS_FINE_LOCATION
                    )
                ) {
                    locationTool.getLocation {
                        jsBridge.locationStr = "${it.latitude},${it.longitude},${it.address}"
                    }
                }
            }

            override fun onDenied(permissionsDeniedForever: List<String>, permissionsDenied: List<String>) {
                LogUtils.d(permissionsDeniedForever, permissionsDenied)
            }
        }).request()
        mPresenter?.other("测试", "Test", "I")
    }

    override fun initData() {
        super.initData()
        viewChange?.setThreeClick { showInputDialog() }
        webView.setOnLongClickListener { true }
        webView.addJavascriptInterface(jsBridge, "jsBridge")//js映射
        webProgress.setColor(ContextCompat.getColor(this, R.color.colorAccent))
        WebViewTool.setWebData(getWebUrl(), webView, webProgress)
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
            WebViewTool.setWebData(getWebUrl(), webView, webProgress)
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
