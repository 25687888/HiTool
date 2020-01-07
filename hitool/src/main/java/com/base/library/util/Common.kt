package com.base.library.util

import android.content.Context
import android.content.res.Resources
import android.os.SystemClock
import android.util.TypedValue
import android.view.View
import java.io.BufferedReader
import java.io.InputStreamReader

val Float.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
    )

val Int.dp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
    ).toInt()


val Float.sp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics
    )


val Int.sp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics
    ).toInt()

fun tryCatch(tryBlock: () -> Unit, catchBlock: (Throwable) -> Unit = {}) {
    try {
        tryBlock()
    } catch (t: Throwable) {
        t.printStackTrace()
        catchBlock(t)
    }
}

/**
 * 从assets文件中读取数据
 */
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

/**
 * 过滤快速导致重复
 * true重复
 * false没有重复
 */
private var mLastClickTime: Long = 0
private var MIN_CLICK_DELAY_TIME = 500
fun isFast(delayTime: Int = 500): Boolean {
    MIN_CLICK_DELAY_TIME = delayTime
    val currentTime = System.currentTimeMillis() // 当前时间
    val time = currentTime - mLastClickTime // 两次时间差
    if (time in 1 until MIN_CLICK_DELAY_TIME) return true
    mLastClickTime = currentTime
    return false
}


/**
 * 扩展函数，View设置三击事件
 */
private val mHits = LongArray(3)

fun View.setThreeClick(listenerBlock: () -> Unit) {
    this.setOnClickListener {
        System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
        mHits[mHits.size - 1] = SystemClock.uptimeMillis()//获取手机开机时间
        if (mHits[mHits.size - 1] - mHits[0] < 500) {
            listenerBlock()//三击事件
        }
    }
}