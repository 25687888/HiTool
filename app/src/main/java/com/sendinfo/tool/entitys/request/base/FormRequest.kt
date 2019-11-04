package com.sendinfo.tool.entitys.request.base

import com.sendinfo.tool.tools.getShebeiCode
import java.io.Serializable

/**
 * 作用: form表单形式提交参数，键值对请求参数
 */
class FormRequest : Serializable {
    var terminalCode: String? = null
    init {
        terminalCode = getShebeiCode()
    }
}
