package com.sendinfo.tool.module

import com.base.library.mvp.BPresenter
import com.base.library.mvp.BasePresenter
import com.base.library.mvp.BaseView
import com.base.library.util.WebViewTool
import com.sendinfo.tool.base.BActivity
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.b_titlebar.*
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import com.sendinfo.tool.R


class WebMvpActivity : BActivity<BPresenter>(), BaseView {

    //        var url = "https://voice.starhonour.com/MobilesXinQiao.html"
    var url = "https://www.xfyun.cn/services/voicedictation"//科大讯飞官网地址
//    var url = "https://api.chafang.me/iat_ws_js_demo/src/index.html"

    override fun bindPresenter(): BPresenter = BasePresenter(this)

    override fun setContentView(): Int = R.layout.activity_web

    override fun initView() {
        tvCenter?.text = "网页测试"
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
        WebViewTool.setWebData(url, webView, null)
    }

    override fun bindData(bodyStr: String) {
    }

    override fun bindError(string: String) {
    }
}
