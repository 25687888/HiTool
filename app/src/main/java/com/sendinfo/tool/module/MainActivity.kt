package com.sendinfo.tool.module

import com.base.library.http.HttpDto
import com.base.library.mvp.BPresenter
import com.base.library.mvp.BasePresenter
import com.base.library.mvp.BaseView
import com.sendinfo.tool.R
import com.sendinfo.tool.base.BaseActivity
import com.sendinfo.tool.entitys.event.EventBean
import com.sendinfo.tool.entitys.request.base.BodyRequest
import com.sendinfo.tool.entitys.request.base.FormRequest
import com.sendinfo.tool.template.ui.DemoActivity
import com.sendinfo.wuzhizhou.utils.GetTicket
import kotlinx.android.synthetic.main.b_titlebar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.intentFor
import talex.zsw.basecore.util.JsonTool

class MainActivity : BaseActivity<BPresenter>(), BaseView {

    override fun setContentView(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        mPresenter = BasePresenter(this)
        tvCenter.text = "首页"
        tvCenter.setOnClickListener {
            startActivity(intentFor<DemoActivity>())
            EventBus.getDefault().postSticky(EventBean(EventBean.CloseBuyPage))
        }
    }

    override fun bindData(bodyStr: String) {
    }

    override fun bindError(string: String) {
    }

}
