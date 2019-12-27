package com.sendinfo.tool.base

import android.content.Context
import com.base.library.base.BApplication
import com.blankj.utilcode.util.ProcessUtils
import com.sendinfo.tool.tools.putIp
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.crashreport.CrashReport.UserStrategy
import android.widget.Toast
import com.blankj.utilcode.util.LogUtils
import com.tencent.bugly.beta.interfaces.BetaPatchListener
import java.util.*


class Application : BApplication() {
    override fun onCreate() {
        super.onCreate()
        putIp("http://192.168.66.205:9090")//设置默认服务器地址
        initTinker(applicationContext)//初始化Bugly热更新、版本升级、Crash统计
    }

    private fun initTinker(mContext: Context) {
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true
        // 设置是否自动下载补丁，默认为true
        Beta.canAutoDownloadPatch = true
        // 设置是否自动合成补丁，默认为true
        Beta.canAutoPatch = true
        // 补丁更新成功，是否提示用户重启，默认为false
        Beta.canNotifyUserRestart = false
        // 补丁回调接口
        Beta.betaPatchListener = object : BetaPatchListener {
            override fun onPatchReceived(patchFile: String) {
                LogUtils.i("补丁下载地址：$patchFile")
            }

            override fun onDownloadReceived(savedLength: Long, totalLength: Long) {
                LogUtils.i("补丁下载进度：${String.format(Locale.getDefault(), "%s %d%%", Beta.strNotificationDownloading, (if (totalLength == 0L) 0 else savedLength * 100 / totalLength).toInt())}")
            }

            override fun onDownloadSuccess(msg: String) {
                LogUtils.i("补丁下载成功")
            }

            override fun onDownloadFailure(msg: String) {
                LogUtils.i("补丁下载失败")
            }

            override fun onApplySuccess(msg: String) {
                LogUtils.i("补丁更新成功")
            }

            override fun onApplyFailure(msg: String) {
                LogUtils.i("补丁更新失败")
            }

            override fun onPatchRollback() {
            }
        }

        // 设置开发设备，默认为false，上传补丁如果下发范围指定为“开发设备”，需要调用此接口来标识开发设备
        Bugly.setIsDevelopmentDevice(mContext, false)
        // 多渠道需求塞入
        // String channel = WalleChannelReader.getChannel(getApplication());
        // Bugly.setAppChannel(getApplication(), channel);
        val strategy = UserStrategy(this).apply { isUploadProcess = ProcessUtils.isMainProcess() }//自定义跨进程日志上报策略，主进程上传日志
        Bugly.init(mContext, "0734b07b1f", true,strategy)//appId替换成你的在Bugly平台申请的appId
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Beta.installTinker()// 安装热更新tinker
    }
}