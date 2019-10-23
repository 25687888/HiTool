package com.sendinfo.tool.entitys.request

import com.sendinfo.wuzhizhou.utils.getShebeiCode
import talex.zsw.basecore.util.TimeTool

/**
 * 日志上传
 */
class UploadLog {
    var terminalCode: String? = null
    var logType: Int = 0
    var logTime: String? = null
    var logLevel: Int = 0
    var logContent: String? = null
    var isOk: Int = 0
    var operatorCode: String? = null
    var remark: String? = null

    init {
        terminalCode = getShebeiCode()
        logTime = TimeTool.getCurTimeString()
        logLevel = 3
        logContent = ""
        operatorCode = ""
        remark = ""
    }
}
