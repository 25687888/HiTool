package com.base.library.mvp

import com.base.library.http.HttpDto
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.lzy.okgo.model.Response

class BModelImpl : BModel {

    override fun getData(callback: BRequestCallback, http: HttpDto) {
        val reqBody = http.getReqMessage()
        val startTime = TimeUtils.getNowMills()
        callback.other(reqBody, "请求参数 ${http.method}", "I")
        http.getOkGo().execute(object : BCallback(callback, http.silence) {
            override fun onSuccess(response: Response<String>?) {
                super.onSuccess(response)
                val rspBody = response?.body() ?: ""
                val rspTime = TimeUtils.getNowMills() - startTime
                printLog(rspTime, reqBody, rspBody)
                callback.other(rspBody, "请求成功 ${http.method}", "I")
                callback.requestSuccess(rspBody, http)
            }

            override fun onError(response: Response<String>?) {
                val errorMsg = "${response?.exception?.message}"
                callback.other(errorMsg, "请求失败 ${http.method}", "E")
                callback.requestError(response?.exception, http)
                val rspTime = TimeUtils.getNowMills() - startTime
                printLog(rspTime, reqBody, errorMsg)
                super.onError(response)
            }
        })
    }

    private fun printLog(rspTime: Long, requestBody: String, rspBody: String) {
        LogUtils.i("$requestBody\n 响应(耗时${rspTime}ms) :\n$rspBody")
    }
}