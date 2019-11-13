package com.base.library.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import com.base.library.database.DataBaseUtils
import com.base.library.database.entity.JournalRecord
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 *  @desc:工具类,扩展函数及常用的内联函数
 */
//添加日志记录到数据库
fun roomInsertJournalRecord(content: String, behavior: String, level: String): Single<Long> = DataBaseUtils
    .getJournalRecordDao()
    .insertRxCompletable(
        JournalRecord().apply {
            this.content = content
            this.behavior = behavior
            this.level = level
        })
    .subscribeOn(Schedulers.newThread())
    .observeOn(AndroidSchedulers.mainThread())

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
private const val MIN_CLICK_DELAY_TIME = 800
fun isFast(): Boolean {
    val currentTime = System.currentTimeMillis() // 当前时间
    val time = currentTime - mLastClickTime // 两次时间差
    if (time in 1 until MIN_CLICK_DELAY_TIME) return true
    mLastClickTime = currentTime
    return false
}