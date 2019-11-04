package com.sendinfo.wuzhizhou.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import java.io.BufferedReader
import java.io.InputStreamReader

inline fun <reified T : Activity> Activity.startAct(isFinish: Boolean = true) {
    val intent = Intent(this, T::class.java)
    this.startActivity(intent)
    if (isFinish) this.finish()
}

fun startAct(context: Context, intent: Intent, isFinish: Boolean = true) {
    context.startActivity(intent)
    if (isFinish && context is Activity) context.finish()
}

// 默认打印模板
fun defaultTemplate(): String {
    return "CUT  ON \n" +   //切纸
//    return "CUT  OFF \n" +    //不切纸
            "DIR3\n" +
            "NASC \"UTF-8\"\n" +
            "FT \"Microsoft YaHei\",6,0,145\n" +
//            "FT \"FangSong\",6,0,145\n" +

            "PP600,190 \n" +
            "BARSET \"QRCODE\",1,1,6,2,1:" +
            "PB \"barCode\" \n" +

            "PP450,80\n" +
            "PT \"人数: 1 \"\n" +

            "PP450,110\n" +
            "PT \"名称: 张三李四王二麻子 \"\n" +

            "PP450,170\n" +
            "PT \"生效日期 : 2019-09-24 \"\n" +

            "PP450,200\n" +
            "PT \"票价: 1.0 \"\n" +
            "PF1\n"
}

fun getFromAssets(context: Context, fileName: String): String {
    val sb = StringBuilder()
    try {
        val inputReader = InputStreamReader(context.resources.assets.open(fileName))
        val bufReader = BufferedReader(inputReader)
        var line: String?
        while (true) {
            line = bufReader.readLine()
            if (line == null) break
            else sb.append(line)
        }
        return sb.toString()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return sb.toString()
}
