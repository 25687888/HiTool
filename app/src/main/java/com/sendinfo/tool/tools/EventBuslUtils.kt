package com.sendinfo.tool.tools

import androidx.lifecycle.LifecycleOwner
import com.base.library.interfaces.MyLifecycleObserver
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * EventBus工具类简易封装
 */
class EventBuslUtils : MyLifecycleObserver {
    private lateinit var owner: LifecycleOwner

    override fun onCreate(owner: LifecycleOwner) {
        this.owner = owner
        EventBus.getDefault().register(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
    }

    override fun onStop(owner: LifecycleOwner) {
    }

    override fun onDestroy(owner: LifecycleOwner) {
        EventBus.getDefault().unregister(owner)
    }
}