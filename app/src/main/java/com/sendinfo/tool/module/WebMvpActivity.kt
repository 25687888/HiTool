package com.sendinfo.tool.module

import com.base.library.mvp.BPresenter
import com.base.library.mvp.BasePresenter
import com.base.library.mvp.BaseView
import com.base.library.util.WebViewTool
import com.sendinfo.tool.R
import com.sendinfo.tool.base.BActivity
import kotlinx.android.synthetic.main.activity_web.*

class WebMvpActivity : BActivity<BPresenter>(), BaseView {

    var url = "https://voice.starhonour.com/MobilesXinQiao.html"

    override fun bindPresenter(): BPresenter = BasePresenter(this)

    override fun setContentView(): Int = R.layout.activity_web

    override fun initView() {
    }

    override fun initData() {
        super.initData()
        WebViewTool.setWebData(url, webView, null)
//        setWebData(url,webView,null,null)
    }

    override fun bindData(bodyStr: String) {
    }

    override fun bindError(string: String) {
    }

}
