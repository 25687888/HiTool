package com.base.library.util

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset

class LogInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val startTime = TimeUtils.getNowMills()
        val original = chain.request()
        val response = chain.proceed(original)
        val rspTime = TimeUtils.getNowMills() - startTime
        LogUtils.i(
            String.format(
                "%s\n接口地址：%s\n响应时长：%dms\n请求头：%s\n请求体：%s\n响应体：%s",
                TimeUtils.getNowString(),
                original.url(),
                rspTime,
                getRequestHeaders(original),
                getRequestInfo(original),
                getResponseInfo(response)
            )
        )
        return response
    }

    /**
     * 打印请求头
     *
     * @param request 请求的对象
     */
    private fun getRequestHeaders(request: Request?): String {
        val str = ""
        if (request == null) {
            return str
        }
        val headers = request.headers() ?: return str
        return headers.toString()
    }

    /**
     * 打印请求消息
     *
     * @param request 请求的对象
     */
    private fun getRequestInfo(request: Request?): String {
        var str = ""
        if (request == null) {
            return str
        }
        val requestBody = request.body() ?: return str
        try {
            val bufferedSink = Buffer()
            requestBody.writeTo(bufferedSink)
            val charset = Charset.forName("utf-8")
            str = bufferedSink.readString(charset)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return str
    }

    /**
     * 打印返回消息
     *
     * @param response 返回的对象
     */
    private fun getResponseInfo(response: Response?): String {
        var str = ""
        if (response == null || !response.isSuccessful) {
            return str
        }
        val responseBody = response.body()
        val contentLength = responseBody!!.contentLength()
        val source = responseBody.source()
        try {
            source.request(10 * 1024.toLong()) // Buffer the entire body.
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val buffer = source.buffer()
        val charset = Charset.forName("utf-8")
        if (contentLength != 0L) {
            str = buffer.clone().readString(charset)
        }
        return str
    }

    companion object {
        var isOutLogHttpDao = true//是否在httpDato输出网络请求、响应日志
        var isOutLogInterceptor = false//是否在拦截器中输出网络请求、响应日志
    }
}