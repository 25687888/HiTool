package com.sendinfo.tool.tools

import com.base.library.http.HttpDto
import com.base.library.util.SpTool

/**
 * 设备编码
 */
fun putShebeiCode(shebeiCode: String) {
    SpTool.saveString("shebeiCode", shebeiCode)
}

fun getShebeiCode(): String = SpTool.getString("shebeiCode")

/**
 * 可出票数
 */
fun putPrintNumber(printNumber: Int) {
    SpTool.saveInt("printNumber", printNumber)
}

fun getPrintNumber(): Int = SpTool.getInt("printNumber", 0)

/**
 * 选择身份证设备,1 维尔,2 HID
 */
fun putIdCard(idCard: Int) {
    SpTool.saveInt("idCard", idCard)
}

fun getIdCard(): Int = SpTool.getInt("idCard", 1)

/**
 * 选择打印机,1 霍尼,2 TSC
 */
fun putDyj(dyj: Int) {
    SpTool.saveInt("dyj", dyj)
}

fun getDyj(): Int = SpTool.getInt("dyj", 1)

/**
 * IP地址
 */
fun putIp(ip: String) {
    SpTool.saveString(HttpDto.DefaultIp, ip)
}

fun getIp(): String = SpTool.getString(HttpDto.DefaultIp)

/**
 * 获取二维码串口
 */
fun putQRcodeSerialPort(QRcodeSerialPort: String) {
    SpTool.saveString("QRcodeSerialPort", QRcodeSerialPort)
}

fun getQRcodeSerialPort(): String = SpTool.getString("QRcodeSerialPort")

/**
 * 指示灯控制板
 */
fun putIcCardSerialPort(IcCardSerialPort: String) {
    SpTool.saveString("IcCardSerialPort", IcCardSerialPort)
}

fun getIcCardSerialPort(): String = SpTool.getString("IcCardSerialPort")

/**
 * 手势密码
 */
fun putPlv(plv: String) {
    SpTool.saveString("plv", plv)
}

fun getPlv(): String = SpTool.getString("plv", "")

/**
 * 一次取票数最大限制
 */
fun putTakeNumber(takeNumber: Int) {
    SpTool.saveInt("takeNumber", takeNumber)
}

fun getTakeNumber(): Int = SpTool.getInt("takeNumber", 100)

