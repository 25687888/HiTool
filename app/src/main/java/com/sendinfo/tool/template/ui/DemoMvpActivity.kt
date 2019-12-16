package com.sendinfo.tool.template.ui

import com.base.library.http.HttpDto
import com.base.library.util.JsonTool
import com.base.library.view.other.RxToast
import com.base.library.view.sweetdialog.BSweetAlertDialog
import com.sendinfo.tool.template.contract.DemoContract
import com.sendinfo.tool.template.presenter.DemoPresenter
import com.sendinfo.tool.R
import com.sendinfo.tool.base.BActivity
import com.sendinfo.tool.entitys.event.EventBean
import com.sendinfo.tool.entitys.request.base.BodyRequest
import com.sendinfo.tool.entitys.request.base.FormRequest
import com.sendinfo.tool.tools.GetTicket
import org.greenrobot.eventbus.Subscribe

/**
 * 作用: 使用案例,Activity使用自己定义的Contract和Presenter
 */
class DemoMvpActivity : BActivity<DemoContract.Presenter>(), DemoContract.View {

    override fun bindPresenter(): DemoPresenter = DemoPresenter(this)

    override fun setContentView(): Int = R.layout.activity_test_template

    override fun initData() {
        //播放声音示例
        soundPoolTool?.startPlayVideo(R.raw.area)
        //发起网络请求示例 body方式
        var httpDto = HttpDto(GetTicket).apply { bodyJson = JsonTool.getJsonString(BodyRequest()) }
        mPresenter?.getData(httpDto)

        //发起网络请求示例  键值对方式
        var httpDto1 = HttpDto(GetTicket).apply { params = JsonTool.getMapFromObj(FormRequest()) }
        mPresenter?.getData(httpDto1)
    }

    override fun loginSuccess(request: String?) {
        RxToast.showToast("登录成功")
    }

    override fun loginError(msg: String?) {
        showDialog(BSweetAlertDialog.ERROR_TYPE, msg, "", cancelListener = null)
    }

    @Subscribe(sticky = true)
    fun onEvent(event: EventBean) {
        RxToast.showToast(event.action)
    }
}