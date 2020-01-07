package com.base.library.mvp
import com.base.library.http.HttpDto
/**
 * 作用: 网络请求监听基类和声明周期管理
 */
interface BRequestCallback : BPresenter{
    /**
     * 请求之前调用
     */
    fun beforeRequest()

    /**
     * 请求错误调用
     */
    fun requestError(throwable: Throwable?, baseHttpDto: HttpDto)

    /**
     * 请求完成调用
     */
    fun requestComplete()

    /**
     * 返回成功调用 返回数据
     */
    fun requestSuccess(body: String, baseHttpDto: HttpDto)
}
