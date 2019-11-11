package com.sendinfo.tool.entitys.request

import com.blankj.utilcode.util.TimeUtils
import com.sendinfo.tool.tools.getShebeiCode

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
        logTime = TimeUtils.getNowString()
        logLevel = 3
        logContent = ""
        operatorCode = ""
        remark = ""
    }
}
