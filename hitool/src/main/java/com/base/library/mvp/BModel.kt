package com.base.library.mvp

import com.base.library.http.HttpDto
import io.reactivex.disposables.Disposable

/**
 * 作用: 基本的Model类，简单的用来获取一个网络数据
 */
interface BModel {

    fun getData(callback: BRequestCallback, http: HttpDto)

    fun getOkGoRx(callback: BRequestCallback, http: HttpDto)

    fun getRetrofit2(callback: BRequestCallback, http: HttpDto)

    fun addDispose(disposable: Disposable)

    fun closeAllDispose()

}
