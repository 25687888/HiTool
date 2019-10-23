package com.base.library.util

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.LifecycleOwner
import com.base.library.interfaces.MyLifecycleObserver
import talex.zsw.basecore.util.Tool

/**
 * 短音频 + 震动
 */
class SoundPoolUtils : MyLifecycleObserver {

    private lateinit var owner: LifecycleOwner

    override fun onCreate(owner: LifecycleOwner) {
        this.owner = owner
    }

    override fun onDestroy(owner: LifecycleOwner) {
        release()
    }

    private val MAX_STREAMS = 2
    private val DEFAULT_QUALITY = 0
    private val DEFAULT_PRIORITY = 1
    private val LEFT_VOLUME = 1
    private val RIGHT_VOLUME = 1
    private val LOOP = 0
    private val RATE = 1.0f

    private lateinit var mSoundPool: SoundPool
    private lateinit var mVibrator: Vibrator

    init {
        initSoundPool()
        initVibrator()
    }

    /**
     * 初始化短音频
     */
    private fun initSoundPool() {
        mSoundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder().setMaxStreams(MAX_STREAMS).build()
        } else {
            SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, DEFAULT_QUALITY)
        }
    }

    /**
     * 初始化震动
     */
    private fun initVibrator() {
        mVibrator = Tool.getContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    /**
     * resId 音频的资源ID
     */
    fun startPlayVideo(resId: Int) {
        val load = mSoundPool.load(Tool.getContext(), resId, DEFAULT_PRIORITY)
        mSoundPool.setOnLoadCompleteListener { soundPool, sampleId, status ->
            mSoundPool.play(load, LEFT_VOLUME.toFloat(), RIGHT_VOLUME.toFloat(), DEFAULT_PRIORITY, LOOP, RATE)
        }
    }

    /**
     * milliseconds 震动时间
     */
    fun startVibrator(milliseconds: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createOneShot(milliseconds, 100)
            mVibrator.vibrate(vibrationEffect)
        } else {
            mVibrator.vibrate(1000)
        }
    }

    /**
     * @param resId        资源id
     * @param milliseconds 震动时间
     * 方法描述: 同时开始音乐和震动
     */
    fun startVideoAndVibrator(resId: Int, milliseconds: Long) {
        startPlayVideo(resId)
        startVibrator(milliseconds)
    }

    /**
     * 方法描述:  释放相应的资源
     */
    fun release() {
        mSoundPool.release()
        mVibrator.cancel()
    }

}