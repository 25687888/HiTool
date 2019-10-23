package com.sendinfo.tool.entitys.event

import java.io.Serializable

class EventBean(var action: String) : Serializable {
    companion object {
        val CloseMaintainPage = "closeMaintainPage"
        val CloseBuyPage = "closeBuyPage"
        val closeSelectArea = "closeSelectArea"
    }
}