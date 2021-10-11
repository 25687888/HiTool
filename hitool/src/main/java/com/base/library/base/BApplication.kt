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
import com.base.library.util.tryCatch
import com.lzy.okgo.cache.CacheEntity
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.cookie.CookieJarImpl
import com.lzy.okgo.cookie.store.SPCookieStore
import com.lzy.okgo.model.HttpHeaders
import com.lzy.okgo.model.HttpParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


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
//        val sslParams1 = HttpsUtils.getSslSocketFactory()
//        val builder = OkHttpClient.Builder()
//        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager)
//
//        重连次数,默认三次,最差的情况4次(一次原始请求,三次重连请求),不需要可以设置为0
//        OkGo.getInstance().init(this).setOkHttpClient(builder.build()).retryCount = 0

        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        val headers = HttpHeaders()
        headers.put("Connection","close")
        //		headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        //		headers.put("commonHeaderKey2", "commonHeaderValue2");
        val params = HttpParams()
        //		params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        //		params.put("commonParamsKey2", "这里支持中文参数");
        //-------------------------------------------------------------------------------------//
        val builder = OkHttpClient.Builder()
        //全局的读取超时时间
        builder.readTimeout(10 * 1000, TimeUnit.MILLISECONDS)
        //全局的写入超时时间
        builder.writeTimeout(10 * 1000, TimeUnit.MILLISECONDS)
        //全局的连接超时时间
        builder.connectTimeout(10 * 1000, TimeUnit.MILLISECONDS)

        //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(CookieJarImpl(SPCookieStore(this)))
        builder.retryOnConnectionFailure(false)
        //方法一：信任所有证书,不安全有风险
        val sslParams1 = HttpsUtils.getSslSocketFactory()
        OkGo.getInstance().init(this)                       //必须调用初始化
            .setOkHttpClient(builder.build())               //必须设置OkHttpClient
            .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
            .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
            .setRetryCount(0)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
            .addCommonHeaders(headers)                      //全局公共头
            .addCommonParams(params)                      //全局公共参数
        OkGo.getInstance().cancelAll()
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
