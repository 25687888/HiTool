package com.sendinfo.tool.template.contract

import com.base.library.mvp.BPresenter
import com.base.library.mvp.BView

/**
 * 作用: 使用案例,自己定义Contract
 */
interface DemoContract {

    interface View : BView {
        fun loginSuccess(request: String?)

        fun loginError(msg: String?)
    }

    interface Presenter : BPresenter {
        fun check(idCard: String)
    }

}