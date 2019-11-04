package com.sendinfo.tool.template.ui

import com.base.library.mvp.BPresenter
import com.base.library.mvp.BasePresenter
import com.base.library.mvp.BaseView
import com.sendinfo.tool.R
import com.sendinfo.tool.base.BActivity

/**
 * 作用: 使用案例,使用通用的P和V
 */
class CommonMvpActivity : BActivity<BPresenter>(), BaseView {

    override fun bindPresenter(): BPresenter = BasePresenter(this)

    override fun setContentView(): Int = R.layout.activity_test_template

    override fun bindData(bodyStr: String) {
    }

    override fun bindError(string: String) {
    }
}