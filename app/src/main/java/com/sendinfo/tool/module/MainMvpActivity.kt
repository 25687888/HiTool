package com.sendinfo.tool.module

import com.base.library.mvp.BPresenter
import com.base.library.mvp.BasePresenter
import com.base.library.mvp.BaseView
import com.sendinfo.tool.R
import com.sendinfo.tool.base.BActivity
import com.sendinfo.tool.entitys.event.EventBean
import com.sendinfo.tool.template.presenter.DemoPresenter
import com.sendinfo.tool.template.ui.DemoMvpActivity
import kotlinx.android.synthetic.main.b_titlebar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.intentFor
import talex.zsw.basecore.util.LogTool
import talex.zsw.basecore.util.PermissionConstants
import talex.zsw.basecore.util.PermissionHelper
import talex.zsw.basecore.util.PermissionTool

class MainMvpActivity : BActivity<BPresenter>(), BaseView {

    override fun setContentView(): Int = R.layout.activity_main

    override fun bindPresenter(): BPresenter = BasePresenter(this)

    override fun initView() {
        PermissionHelper.requestStorage(object : PermissionTool.FullCallback {
            override fun onGranted(permissionsGranted: MutableList<String>?) {
            }
            override fun onDenied(permissionsDeniedForever: MutableList<String>?, permissionsDenied: MutableList<String>?) {
                PermissionHelper.showOpenAppSettingDialog()
            }
        })
        tvCenter.text = "首页"
        tvCenter.setOnClickListener {
            startActivity(intentFor<DemoMvpActivity>())
            EventBus.getDefault().postSticky(EventBean(EventBean.CloseBuyPage))
        }
    }
    override fun bindData(bodyStr: String) {
    }

    override fun bindError(string: String) {
    }

}
