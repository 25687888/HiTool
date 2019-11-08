package com.base.library.http

import android.text.TextUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.convert.StringConvert
import com.lzy.okgo.request.base.BodyRequest
import com.lzy.okgo.request.base.Request
import com.lzy.okrx2.adapter.ObservableBody
import io.reactivex.Observable
import talex.zsw.basecore.util.JsonTool
import talex.zsw.basecore.util.LogTool
import talex.zsw.basecore.util.SpTool

/**
 * 通用的网络请求参数封装 method 请求的标志
 */
class HttpDto(val method: String) {
    var httpType = POST //请求类型
    var httpMode = getOkGo //请求方式
    var silence = false //是否静默加载
    var isFinish = false//请求失败 确定 提示框 是否销毁当前页面
    var isSpliceUrl = false//是否强制将params的参数拼接到url后面,up系列与params系列混用
    var isMultipart = false//是否强制使用multipart/form-data表单上传
    var fullUrl: String = ""//完整的URL
    var url: String = "${SpTool.getString(DefaultIp)}$method" //方法名(默认设置为URL)
    var extendField: Any? = null//扩展字段（作用如：同一个接口标记不同方法名）
    var cacheMode = CacheMode.NO_CACHE//缓存模式
    var cacheTime = -1L //缓存时长 -1永不过期
    var heads: Map<String, String>? = null //请求头和参数
    var params: Map<String, String>? = null // key value 参数
    var bodyJson: String = "" //bodyJson
    var bodyString: String = "" //upString
    var tag: Any? = null //标识
    fun getOkGo(): Request<String, out Request<*, *>> {
        return getRequest()
    }

    fun getOkGoRx(): Observable<String> {
        val request = getRequest()
        request.converter(StringConvert())
        return request.adapt(ObservableBody<String>())
    }

    private fun getRequest(): Request<String, out Request<*, *>> {
        if (!TextUtils.isEmpty(fullUrl)) url = fullUrl
        val request: Request<String, out Request<*, *>>
        when (httpType) {
            GET -> request = OkGo.get(url)
            POST -> request = OkGo.post(url)
            PUT -> request = OkGo.put(url)
            DELETE -> request = OkGo.delete(url)
            HEAD -> request = OkGo.head(url)
            else -> request = OkGo.options(url)
        }
        heads?.forEach { request?.headers(it.key, it.value) }
        request?.params(params)
        request?.tag(tag)
        request?.cacheMode(cacheMode)
        request?.cacheTime(cacheTime)

        if (!TextUtils.isEmpty(bodyJson) || !TextUtils.isEmpty(bodyString) || isSpliceUrl) {//是否强制将params的参数拼接到url后面,up系列与params系列混用
            (request as BodyRequest).upString(bodyString)
            request.upString(bodyString)
            request.upJson(bodyJson)
            request.isSpliceUrl(isSpliceUrl)
            request.isMultipart(isMultipart) // 是否使用表单上传
        }

        return request
    }

    fun print(): String {
        val sb = StringBuilder()
        sb.appendln("请求地址 : $url")
        sb.appendln("请求方法 : $method")
        sb.appendln("扩展字段 : ${JsonTool.getJsonString(extendField)}")
        sb.appendln("params参数为 : ")
        params?.forEach { sb.appendln("${it.key} = ${it.value}") }
        sb.appendln("请求头为 : ")
        heads?.forEach { sb.appendln("${it.key} = ${it.value}") }
        sb.appendln("body参数为 : ")
        LogTool.i(sb.toString())
        if (!TextUtils.isEmpty(bodyJson)) JsonTool.getJsonString(bodyJson)

        sb.appendln(bodyJson)
        return sb.toString()
    }

    companion object {
        const val GET = 0x100000
        const val POST = 0x100001
        const val PUT = 0x100002
        const val DELETE = 0x100003
        const val HEAD = 0x100004
        const val OPTIONS = 0x100005
        const val getOkGo = 0x100006 // OkGo 请求方式
        const val getOkGoRx = 0x100007 // OkGo Rx 请求方式
        const val DefaultIp = "DefaultIp" //默认ip地址KEY
    }
}