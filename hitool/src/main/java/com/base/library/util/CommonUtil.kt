package com.base.library.util

import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import com.base.library.database.DataBaseUtils
import com.base.library.database.entity.JournalRecord
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *  @desc:   工具类
 */
inline fun tryCatch(tryBlock: () -> Unit, catchBlock: (Throwable) -> Unit = {}) {
    try {
        tryBlock()
    } catch (t: Throwable) {
        t.printStackTrace()
        catchBlock(t)
    }
}

//添加日志记录到数据库
inline fun roomInsertJournalRecord(content: String, behavior: String, level: String): Single<Long> = DataBaseUtils
    .getJournalRecordDao()
    .insertRxCompletable(JournalRecord().apply {
        this.content = content
        this.behavior = behavior
        this.level = level
    })
    .subscribeOn(Schedulers.newThread())
    .observeOn(AndroidSchedulers.mainThread())

//变换 IO线程 -> Main线程
inline fun <T> transformer(): ObservableTransformer<T, T> = ObservableTransformer {
    it.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
}

/**
 * 直接将runable抛向主线程
 */
inline fun runOnUiThread(r: Runnable) {
    Handler(Looper.getMainLooper()).post(r)
}


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