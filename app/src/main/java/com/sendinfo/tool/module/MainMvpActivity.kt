package com.sendinfo.tool.module

import com.base.library.mvp.BPresenter
import com.base.library.mvp.BasePresenter
import com.base.library.mvp.BaseView
import com.base.library.util.glide.GlideApp
import com.base.library.view.other.RxToast
import com.base.library.view.sweetdialog.BSweetAlertDialog
import com.sendinfo.tool.R
import com.sendinfo.tool.base.BActivity
import com.sendinfo.tool.template.ui.DemoMvpActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.b_titlebar.*
import org.jetbrains.anko.intentFor

class MainMvpActivity : BActivity<BPresenter>(), BaseView {

    override fun setContentView(): Int = R.layout.activity_main

    override fun bindPresenter(): BPresenter = BasePresenter(this)

    override fun initView() {
        tvCenter.text = "首页"
        RxToast.info("首页")
        tvCenter.setOnClickListener {
            startActivity(intentFor<DemoMvpActivity>())
        }
        showDialog(
            BSweetAlertDialog.NORMAL_TYPE,
            "Test",
            "this is test title text!",
            confirmListener = getConfirmDisListener()
        )
        GlideApp.with(this).load(R.drawable.ic_launcher_background).into(imageView)
    }

    override fun bindData(bodyStr: String) {
    }

    override fun bindError(string: String) {
    }

}
