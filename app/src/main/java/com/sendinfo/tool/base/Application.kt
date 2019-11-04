package com.sendinfo.tool.base

import com.base.library.base.BApplication
import com.sendinfo.tool.tools.putIp

class Application : BApplication() {
    override fun onCreate() {
        super.onCreate()
        putIp("http://192.168.66.205:9090")
    }
}