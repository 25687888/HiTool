package com.base.library.mvp

import com.base.library.http.HttpDto
import com.blankj.utilcode.util.LogUtils
import com.lzy.okgo.model.Response

class BModelImpl : BModel {

    override fun getData(callback: BRequestCallback, http: HttpDto) {
        val requestBody = http.print()
        callback.other(requestBody, "请求参数 ${http.method}", "I")
        http.getOkGo().execute(object : BCallback(callback, http.silence) {
            override fun onSuccess(response: Response<String>?) {
                super.onSuccess(response)
                val body = response?.body() ?: ""
                printLog(http.url, http.method, body)
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
        LogUtils.i(
            StringBuilder()
                .appendln("Response 地址 : $url")
                .appendln("方法 : $method")
                .appendln("返回数据如下 : ")
                .toString()
        )
        LogUtils.json(data)
    }
}