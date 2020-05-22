package com.base.library.mvp

import com.base.library.http.HttpDto
import com.base.library.util.LogInterceptor
import com.blankj.utilcode.util.LogUtils
import com.lzy.okgo.model.Response

class BModelImpl : BModel {

    override fun getData(callback: BRequestCallback, http: HttpDto) {
        if (LogInterceptor.isOutLogHttpDao) {
            val requestBody = http.print()
            callback.other(requestBody, "请求参数 ${http.method}", "I")
        }
        http.getOkGo().execute(object : BCallback(callback, http.silence) {
            override fun onSuccess(response: Response<String>?) {
                super.onSuccess(response)
                val body = response?.body() ?: ""
                if (LogInterceptor.isOutLogHttpDao) {
                    printLog(http.url, http.method, body)
                }
                callback.other(body, "请求成功 ${http.method}", "I")
                callback.requestSuccess(body, http)
            }

            override fun onError(response: Response<String>?) {
                val throwable = response?.exception
                callback.other("${throwable?.message}", "请求失败 ${http.method}", "E")
                callback.requestError(throwable, http)
                super.onError(response)
            }
        })
    }

    private fun printLog(url: String, method: String, data: String) {
        LogUtils.i("Response地址 : $url\n方法 : $method\n返回数据如下 :\n$data")
    }
}