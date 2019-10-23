package com.base.library.mvp
interface BaseView : BView {

    fun bindData(bodyStr: String)

    fun bindError(string: String)

}