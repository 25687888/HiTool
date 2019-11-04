package com.base.library.base

import android.annotation.SuppressLint
import androidx.multidex.MultiDexApplication
import com.base.library.BuildConfig
import com.base.library.util.CockroachUtil
import com.base.library.util.roomInsertJournalRecord
import com.lzy.okgo.OkGo
import com.lzy.okgo.https.HttpsUtils
import okhttp3.OkHttpClient
import talex.zsw.basecore.util.LogTool
import talex.zsw.basecore.util.Tool

/**
 * 作用: 程序的入口
 */
open class BApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        val startTime = System.currentTimeMillis()//获取开始时间
        Tool.init(this, true)//basecore库初始化
        initHttp()
        if (!BuildConfig.DEBUG) initCockroach()
        LogTool.d("BApplication启动耗时(ms): ${System.currentTimeMillis() - startTime}")
    }

    /**
     * 不死异常拦截
     * handlerException内部建议手动try{ 异常处理逻辑 }catch(Throwable e){ }
     * 以防handlerException内部再次抛出异常，导致循环调用handlerException
     */
    private fun initCockroach() {
        CockroachUtil.install(object : CockroachUtil.ExceptionHandler {
            @SuppressLint("CheckResult")
            override fun handlerException(thread: Thread, throwable: Throwable, info: String) {
                try {
                    LogTool.e(info)
                    roomInsertJournalRecord(info, "异常-全局", "E").subscribe({}, {})
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
//        builder.addInterceptor(loggingInterceptor)//打印日志
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager)

        //重连次数,默认三次,最差的情况4次(一次原始请求,三次重连请求),不需要可以设置为0
        OkGo.getInstance().init(this).setOkHttpClient(builder.build()).retryCount = 0
    }
}
