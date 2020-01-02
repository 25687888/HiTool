package com.sendinfo.tool.base

import android.content.Intent
import android.view.LayoutInflater
import com.base.library.base.BMvpActivity
import com.base.library.mvp.BPresenter
import com.sendinfo.tool.tools.EventBuslUtils
import com.base.library.util.SoundPoolTool
import com.base.library.view.sweetdialog.BSweetAlertDialog
import com.sendinfo.standard.tools.SpeechTool
import com.sendinfo.tool.R
import kotlinx.android.synthetic.main.activity_base.*
import org.greenrobot.eventbus.Subscribe

/**
 * BaseActivity封装
 */
abstract class BActivity<T : BPresenter> : BMvpActivity<T>() {

    var soundPoolUtils: SoundPoolTool? = null//语音播放

    var speechTool: SpeechTool? = null//语音合成

    abstract fun setContentView(): Int

    abstract fun bindPresenter(): T

    override fun getSweetAlertDialog(): BSweetAlertDialog? = null//使用默认加载对话框

    /**
     * 页面传值
     */
    override fun initArgs(intent: Intent?) {
    }

    /**
     *初始化页面ID及沉浸式， 页面初始化时调用
     */
    override fun initContentView() {
        setContentView(R.layout.activity_base)
        val contentView = LayoutInflater.from(this).inflate(setContentView(), fl, false)
        fl.addView(contentView)
        mPresenter = bindPresenter()
        initView()
        lifecycle.addObserver(EventBuslUtils())
    }

    /**
     * 页面初始化时调用
     */
    open fun initView() {

    }

    /**
     * 页面初始化完成后调用
     */
    override fun initData() {

    }

    inner class NotingEvent

    @Subscribe
    public fun onEvent(event: NotingEvent) {
    }
}