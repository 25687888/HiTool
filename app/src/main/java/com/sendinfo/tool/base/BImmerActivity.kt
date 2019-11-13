package com.sendinfo.tool.base

import android.content.Intent
import android.view.LayoutInflater
import com.base.library.base.BMvpActivity
import com.base.library.mvp.BPresenter
import com.base.library.util.SoundPoolTool
import com.gyf.immersionbar.ImmersionBar
import com.sendinfo.tool.R
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.b_titlebar.*

/**
 * 沉浸式BaseActivity封装
 */
abstract class BImmerActivity<T : BPresenter> : BMvpActivity<T>() {
    val soundPoolTool: SoundPoolTool by lazy { SoundPoolTool().apply { lifecycle.addObserver(this) } }

    abstract fun setContentView(): Int

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
        ImmersionBar.with(this).titleBar(bTitlebar).init() // 沉浸式
        val contentView = LayoutInflater.from(this).inflate(setContentView(), fl, false)
        fl.addView(contentView)
        initView()
    }

    /**
     * 页面初始化时调用
     */
    open fun initView(){

    }

    /**
     * 页面初始化完成后调用
     */
    override fun initData() {

    }
}