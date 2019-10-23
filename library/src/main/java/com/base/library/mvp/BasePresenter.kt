package com.base.library.mvp

import com.base.library.http.HttpDto
import com.blankj.utilcode.util.LogUtils

/**
 * 作用: 通用的P层实现
 */
class BasePresenter(view: BaseView) : BPresenterImpl<BaseView>(view), BPresenter {

    override fun requestSuccess(body: String, baseHttpDto: HttpDto) {
        super.requestSuccess(body,baseHttpDto)
        mView?.bindData(body)
    }

    override fun requestError(throwable: Throwable?, baseHttpDto: HttpDto) {
        mView?.disDialog()

        var string = "${throwable?.message}"
        LogUtils.e("错误信息 : \n ${throwable?.message}")

        mView?.bindError(string)
    }

}
