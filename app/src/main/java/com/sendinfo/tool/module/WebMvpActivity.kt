package com.sendinfo.tool.module

import com.base.library.mvp.BPresenter
import com.base.library.mvp.BasePresenter
import com.base.library.mvp.BaseView
import com.sendinfo.tool.R
import com.sendinfo.tool.base.BActivity
import kotlinx.android.synthetic.main.activity_web.*
import talex.zsw.basecore.util.LogTool
import talex.zsw.basecore.util.PermissionConstants
import talex.zsw.basecore.util.PermissionHelper.setFullScreen
import talex.zsw.basecore.util.PermissionTool
import talex.zsw.basecore.util.WebViewTool

class WebMvpActivity : BActivity<BPresenter>(), BaseView {

    var url = "https://voice.starhonour.com/MobilesXinQiao.html"

    override fun bindPresenter(): BPresenter = BasePresenter(this)

    override fun setContentView(): Int = R.layout.activity_web

    override fun initView() {
        PermissionTool.permission(
            PermissionConstants.STORAGE,
            PermissionConstants.MICROPHONE,
            PermissionConstants.LOCATION
        ).callback(object : PermissionTool.FullCallback {
            override fun onGranted(permissionsGranted: List<String>) {
                LogTool.d("BaseCore", permissionsGranted)
            }

            override fun onDenied(permissionsDeniedForever: List<String>, permissionsDenied: List<String>) {
                LogTool.d("BaseCore", permissionsDeniedForever, permissionsDenied)
            }
        }).theme { activity -> setFullScreen(activity) }.request()
    }

    override fun initData() {
        super.initData()
        WebViewTool.setWebData(url, webView, null, null)
//        setWebData(url,webView,null,null)
    }

    override fun bindData(bodyStr: String) {
    }

    override fun bindError(string: String) {
    }

}
