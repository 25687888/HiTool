package com.sendinfo.tool.tools

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.base.library.interfaces.MyLifecycleObserver
import com.blankj.utilcode.util.Utils

/**
 * 高德地图签到模式定位，封装
 */
class LocationTool(var mContext: Context) : MyLifecycleObserver {
    private lateinit var owner: LifecycleOwner
    private var locationClient: AMapLocationClient? = null
    private var listenerLocation: ((AMapLocation) -> Unit?)? = null

    override fun onCreate(owner: LifecycleOwner) {
        this.owner = owner
        initLocation()
    }

    override fun onResume(owner: LifecycleOwner) {
    }

    override fun onStop(owner: LifecycleOwner) {
    }

    override fun onDestroy(owner: LifecycleOwner) {
        locationClient?.onDestroy()
    }

    private fun initLocation() {
        locationClient = AMapLocationClient(mContext)
        val option = AMapLocationClientOption()
        /**
         * 设置签到场景，相当于设置为：
         * option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
         * option.setOnceLocation(true);
         * option.setOnceLocationLatest(true);
         * option.setMockEnable(false);
         * option.setWifiScan(true);
         * option.setGpsFirst(false);
         * 其他属性均为模式属性。
         * 如果要改变其中的属性，请在在设置定位场景之后进行
         */
        option.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
        locationClient?.setLocationOption(option)
        locationClient?.setLocationListener { listenerLocation?.invoke(it) }//设置定位监听
    }

    fun getLocation(listenerLocation: (AMapLocation) -> Unit) {
        this.listenerLocation = listenerLocation
        Utils.runOnUiThreadDelayed({ locationClient?.startLocation() }, 100)
    }
}