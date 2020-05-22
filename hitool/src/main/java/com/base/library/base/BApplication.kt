package com.base.library.base

import android.annotation.SuppressLint
import androidx.multidex.MultiDexApplication
import com.base.library.util.CrashTool
import com.base.library.util.SpTool
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.lzy.okgo.OkGo
import com.lzy.okgo.https.HttpsUtils
import okhttp3.OkHttpClient
import android.content.pm.ApplicationInfo
import com.base.library.db.LogDBManager
import com.base.library.db.LogMessage
import com.base.library.util.LogInterceptor
import com.base.library.util.tryCatch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * 作用: 程序的入口
 */
open class BApplication : MultiDexApplication() {
    private val presenterScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.IO + Job()) }
    var isDebug = false
    override fun onCreate() {
        super.onCreate()
        val startTime = System.currentTimeMillis()//获取开始时间
        isDebug =
            applicationInfo != null && applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        if (LogDBManager.isWrite) LogDBManager.init(this)//日志数据库初始化
        initAndroidUtilCode()
        initHttp()
        if (!isDebug) initCockroach()
        LogUtils.d("BApplication启动耗时(ms): ${System.currentTimeMillis() - startTime}")
    }

    /**
     * 不死异常拦截
     * handlerException内部建议手动try{ 异常处理逻辑 }catch(Throwable e){ }
     * 以防handlerException内部再次抛出异常，导致循环调用handlerException
     */
    private fun initCockroach() {
        CrashTool.install(object : CrashTool.ExceptionHandler {
            @SuppressLint("CheckResult")
            override fun handlerException(thread: Thread, throwable: Throwable, info: String) {
                try {
                    LogUtils.e(info)
                    other(info, "异常-全局", "E")
                } catch (e: Throwable) {
                }
            }
        })
    }

    /**
     * 网络请求
     */
    private fun initHttp() {
//        val loggingInterceptor = HttpLoggingInterceptor("OkGo")
//        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY)
//        loggingInterceptor.setColorLevel(Level.INFO)
        //信任所有证书,不安全有风险
        val sslParams1 = HttpsUtils.getSslSocketFactory()
        val builder = OkHttpClient.Builder()
        if (LogInterceptor.isOutLogInterceptor) {
            val loggingInterceptor = LogInterceptor()
            builder.addInterceptor(loggingInterceptor)//打印日志
        }
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager)

        //重连次数,默认三次,最差的情况4次(一次原始请求,三次重连请求),不需要可以设置为0
        OkGo.getInstance().init(this).setOkHttpClient(builder.build()).retryCount = 0
    }

    /**
     * 初始化打印日志
     */
    private fun initAndroidUtilCode() {
        Utils.init(this)
        SpTool.init(this)
        LogUtils.getConfig().setLogSwitch(isDebug)//总开关
            .setConsoleSwitch(isDebug)//控制台开关
            .setGlobalTag("HJ")//全局 Tag
            .setFilePrefix("Log") // Log 文件前缀
            .setBorderSwitch(isDebug)//边框开关
            .stackDeep = 1 //栈深度
    }

    private fun other(content: String, behavior: String, level: String) {
        tryCatch({
            presenterScope.launch {
                LogDBManager.add(LogMessage(content, behavior, level))
            }
        })
    }
}
