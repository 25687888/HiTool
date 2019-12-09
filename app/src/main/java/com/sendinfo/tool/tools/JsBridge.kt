package com.sendinfo.tool.tools

import android.webkit.JavascriptInterface
import com.blankj.utilcode.util.DeviceUtils

class JsBridge {
    var locationStr = "0,0,定位中"
    /**
     * H5调Android设备ID
     * */
    @JavascriptInterface
    fun getUniqueDeviceId(): String {
        return DeviceUtils.getUniqueDeviceId()
    }

    /**
     * H5调Android获取定位经纬度, 返回:经度,纬度,详细位置信息
     * */
    @JavascriptInterface
    fun getLocation(): String {
        return locationStr
    }
}
