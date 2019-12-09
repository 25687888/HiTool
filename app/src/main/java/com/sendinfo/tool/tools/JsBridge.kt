package com.sendinfo.tool.tools

import android.webkit.JavascriptInterface
import com.blankj.utilcode.util.DeviceUtils

class JsBridge {
    /**
     * H5调Android设备ID
     * */
    @JavascriptInterface
    fun getUniqueDeviceId(): String {
        return DeviceUtils.getUniqueDeviceId()
    }
}
