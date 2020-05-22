package com.sendinfo.tool.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import com.base.library.db.LogDBManager
import com.base.library.db.LogMessage
import com.base.library.http.HttpDto
import com.base.library.util.JsonTool
import com.base.library.util.tryCatch
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.sendinfo.tool.entitys.request.base.FormRequest
import com.sendinfo.tool.entitys.request.UploadLog
import com.sendinfo.tool.entitys.request.base.BodyRequest
import com.sendinfo.tool.entitys.response.BaseResponse
import com.sendinfo.tool.tools.Beat
import com.sendinfo.tool.tools.getShebeiCode
import com.sendinfo.tool.tools.logSave
import kotlinx.coroutines.*

/**
 * 后台定时服务：日志30分钟上传和清理一次、心跳60秒一次
 */
class TimerService : Service() {
    private val presenterScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.Default + Job()) }
    private var handlerLog: Handler? = Handler()
    private var handlerBeat: Handler? = Handler()

    override fun onCreate() {
        super.onCreate()
        handleLogs()//处理日志
        handlerBeat()//心跳
    }

    private fun handleLogs() {
        handlerLog?.removeCallbacksAndMessages(null)
        handlerLog?.postDelayed({
            uploadLog()
            cleanLog()
            handlerLog?.let { handleLogs() }
        }, 5 * 1000)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        presenterScope.cancel()
        handlerLog?.removeCallbacksAndMessages(null)
        handlerLog = null
        handlerBeat?.removeCallbacksAndMessages(null)
        handlerLog = null
    }

    /**
     * 删除3天前的日志记录
     */
    @SuppressLint("CheckResult")
    private fun cleanLog() {
        tryCatch({
            presenterScope.launch {
                LogDBManager.deleteDays(-3)
            }
        })
    }

    /**
     * 日志上传
     */
    @SuppressLint("CheckResult")
    private fun uploadLog() {
        val startTime = TimeUtils.string2Millis(
            TimeUtils.getString(
                TimeUtils.getNowString(),
                -60,
                TimeConstants.MIN
            )
        )
        val endTime = TimeUtils.string2Millis(TimeUtils.getNowString())
        presenterScope.launch {
            val logDB = LogDBManager.queryDate(startTime, endTime)
            val uploadLogs = ArrayList<UploadLog>()
            for (logMessage in logDB) {
                val log = UploadLog().apply {
                    logTime = TimeUtils.millis2String(logMessage.time)
                    logContent = logMessage.content
                    operatorCode = getShebeiCode()
                    getLogLevel(logMessage)
                }
                uploadLogs.add(log)
            }
            if (uploadLogs.isNotEmpty()) {
                val requestBody =
                    BodyRequest().apply { dataInfo = JsonTool.getJsonString(uploadLogs) }
                val httpDto =
                    HttpDto(logSave).apply { bodyJson = JsonTool.getJsonString(requestBody) }
                httpDto.getOkGo().execute(object : StringCallback() {
                    override fun onSuccess(response: Response<String>?) {
                        LogUtils.i("上传日志成功,${response?.code()}")
                    }

                    override fun onError(response: Response<String>?) {
                        LogUtils.i("上传日志失败,${response?.code()},${response?.message()}")
                    }
                })
            }
        }
    }

    private fun UploadLog.getLogLevel(logMessage: LogMessage) {
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

    private fun handlerBeat() {
        handlerBeat?.removeCallbacksAndMessages(null)
        handlerBeat?.postDelayed({
            val form = FormRequest().apply { terminalCode = getShebeiCode() }
            val httpDto = HttpDto(Beat).apply {
                httpType = HttpDto.GET
                params = JsonTool.getMapFromObj(form)
            }
            LogUtils.i(httpDto.getReqMessage())
            httpDto.getOkGo().execute(object : StringCallback() {
                override fun onSuccess(response: Response<String>?) {
                    val body = response?.body() ?: ""
                    val response = JsonTool.getObject(body, BaseResponse::class.java)
                }

                override fun onError(response: Response<String>?) {
                    tryCatch({
                        presenterScope.launch {
                            LogDBManager.add(LogMessage(response?.body() ?: "", "异常-心跳", "E"))
                        }
                    })
                }
            })
            handlerBeat?.let { handlerBeat() }
        }, 1 * 1000)
    }
}
