package com.sendinfo.tool.entitys.request.base

import com.sendinfo.tool.tools.getShebeiCode
import talex.zsw.basecore.util.AppTool
import talex.zsw.basecore.util.EncryptTool
import talex.zsw.basecore.util.TimeTool
import talex.zsw.basecore.util.Tool
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
        this.requestTime = TimeTool.getCurTimeString()
        terminalCode = getShebeiCode()
        version = AppTool.getAppVersionName(Tool.getContext())
        val signStr = "appId=$appId&requestTime=$requestTime&version=$version&key=1499327191351"
        sign = EncryptTool.encryptMD5ToString(signStr).toUpperCase()
    }

    override fun toString(): String {
        return "BodyRequest(version='$version', terminalCode='$terminalCode', appId=$appId, requestTime=$requestTime, sign=$sign, dataInfo=$dataInfo)"
    }

}