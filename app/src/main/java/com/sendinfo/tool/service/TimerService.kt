package com.sendinfo.tool.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.text.TextUtils
import com.base.library.database.DataBaseUtils
import com.base.library.database.entity.JournalRecord
import com.base.library.http.HttpDto
import com.base.library.util.roomInsertJournalRecord
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.sendinfo.tool.entitys.request.base.BodyRequest
import com.sendinfo.tool.entitys.request.base.FormRequest
import com.sendinfo.tool.entitys.request.UploadLog
import com.sendinfo.tool.entitys.response.BaseResponse
import com.sendinfo.wuzhizhou.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import talex.zsw.basecore.util.JsonTool
import talex.zsw.basecore.util.LogTool
import talex.zsw.basecore.util.TimeTool

import java.util.ArrayList
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * 后台定时服务：日志30分钟上传和清理一次、心跳60秒一次
 */
class TimerService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @SuppressLint("CheckResult")
    override fun onCreate() {
        super.onCreate()
        Observable
            .interval(30, TimeUnit.MINUTES).subscribe {
                uploadLog()
                cleanLog()
            }
        Observable
            .interval(0, 60, TimeUnit.SECONDS)
            .subscribe {
                if (!TextUtils.isEmpty(getIp())) httpBeat()
            }
    }

    /**
     * 删除3天前的日志记录
     */
    @SuppressLint("CheckResult")
    private fun cleanLog() {
        DataBaseUtils.getJournalRecordDao()
            .deleteFormTime(TimeTool.nDaysAfter(TimeTool.getCurTimeString(), -3))
            .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogTool.d("删除了多少条:$it") //
            }, {
                LogTool.e("删除: $it.localizedMessage")
            })
    }

    /**
     * 日志上传
     */
    @SuppressLint("CheckResult")
    private fun uploadLog() {
        DataBaseUtils.getJournalRecordDao()
            .queryByTime(TimeTool.getCurTimeString(), TimeTool.formatTime(before()))
            .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogTool.d("查询了多少条:${it.size}")
                val logs = ArrayList<UploadLog>()
                for (logMessage in it) {
                    val log = UploadLog().apply {
                        logTime = logMessage.time
                        logContent = logMessage.content
                        operatorCode = getShebeiCode()
                        getLogLevel(logMessage)
                    }
                    logs.add(log)
                }
                if (logs.isNotEmpty()) {
                    val requestBody = BodyRequest().apply { dataInfo = JsonTool.getJsonString(logs) }
                    val httpDto = HttpDto(logSave).apply { bodyJson = JsonTool.getJsonString(requestBody) }
                    httpDto.getOkGo().execute(object : StringCallback() {
                        override fun onSuccess(response: Response<String>?) {
                            LogTool.i("上传日志成功,${response?.code()}")
                        }

                        override fun onError(response: Response<String>?) {
                            LogTool.i("上传日志失败,${response?.code()},${response?.message()}")
                        }
                    })
                }
            }, {})
    }

    private fun UploadLog.getLogLevel(logMessage: JournalRecord) {
        when (logMessage.level) {
            "e", "E" -> {
                logLevel = 1
                remark = "严重程序错误"
            }
            "w", "W" -> {
                logLevel = 2
                remark = "警告信息"
            }
            "i", "I" -> {
                logLevel = 3
                remark = "运行日志"
            }
            "v", "V" -> {
                logLevel = 4
                remark = "日常信息"
            }
            else -> {
                logLevel = 5
                remark = "未知类型"
            }
        }
    }


    /*
    * from表单形式提示数据
    * */
    @SuppressLint("CheckResult")
    private fun httpBeat() {
        val form = FormRequest().apply { terminalCode = getShebeiCode() }
        val bRequest = HttpDto(Beat).apply {
            httpType = HttpDto.GET
            params = JsonTool.getMapFromObj(form)
        }
        bRequest.print()
        bRequest.getOkGoRx()
            .subscribeOn(Schedulers.io())
            .map {
                val response = JsonTool.getObject(it, BaseResponse::class.java)
                if (response.success) {
                } else {
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {
                addLog("${it.message}", "${bRequest.url}异常-心跳")
            })
    }

    @SuppressLint("CheckResult")
    private fun addLog(message: String, behavior: String) {
        roomInsertJournalRecord(message, behavior, "I").subscribe({ LogTool.d("日志添加成功") }, { LogTool.e("日志添加失败") })
    }

    /**
     * 当前时间的前五分钟
     */
    private fun before(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, -5)
        return calendar.time.time
    }
}
