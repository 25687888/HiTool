package com.sendinfo.tool.entitys.request.base

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.TimeUtils
import com.sendinfo.tool.tools.getShebeiCode
import java.io.Serializable

/**
 * 作用: body json请求参数封装
 */
class BodyRequest : Serializable {
    var appId: String? = null
    var requestTime: String? = null
    var sign: String? = null
    var dataInfo: Any? = null
    var terminalCode: String
    var version: String

    init {
        this.appId = "SDANDROIDSELFTERMANIL"
        this.requestTime = TimeUtils.getNowString()
        terminalCode = getShebeiCode()
        version = AppUtils.getAppVersionName()
        val signStr = "appId=$appId&requestTime=$requestTime&version=$version&key=1499327191351"
        sign = EncryptUtils.encryptMD5ToString(signStr).toUpperCase()
    }

    override fun toString(): String {
        return "BodyRequest(version='$version', terminalCode='$terminalCode', appId=$appId, requestTime=$requestTime, sign=$sign, dataInfo=$dataInfo)"
    }

}