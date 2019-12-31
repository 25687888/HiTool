package com.base.library.util

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.base.library.interfaces.MyLifecycleObserver
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.LogUtils
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.Locale

/**
 * Created by Administrator on 2017/9/15.
 * @param filePath 音频文件缓存路径
 * @param context 上下文对象
 */
class SpeechTool(var context: Context, var filePath: String) : TextToSpeech.OnInitListener, MyLifecycleObserver {
    private lateinit var owner: LifecycleOwner
    private var mSoundPool: SoundPool
    private val MAX_STREAMS = 2
    private val DEFAULT_QUALITY = 0
    private val DEFAULT_PRIORITY = 1
    private val LEFT_VOLUME = 1
    private val RIGHT_VOLUME = 1
    private val LOOP = 0
    private val RATE = 1.0f

    override fun onCreate(owner: LifecycleOwner) {
        this.owner = owner
    }

    override fun onResume(owner: LifecycleOwner) {
    }

    override fun onStop(owner: LifecycleOwner) {
    }

    override fun onDestroy(owner: LifecycleOwner) {
        release()
    }

    private val textToSpeech: TextToSpeech?

    init {
        textToSpeech = TextToSpeech(context, this)
        mSoundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder().setMaxStreams(MAX_STREAMS).build()
        } else {
            SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, DEFAULT_QUALITY)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result1 = textToSpeech?.setLanguage(Locale.ENGLISH)
            val result2 = textToSpeech?.setLanguage(Locale.CHINESE)
            if (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED || result2 == TextToSpeech.LANG_MISSING_DATA || result2 == TextToSpeech.LANG_NOT_SUPPORTED) {
                LogUtils.i("HJ", "SpeechUtils 初始化失败")
            } else {
                LogUtils.i("HJ", "SpeechUtils 初始化成功")
            }
        }
    }

    /**
     * videoPath 音频的文件路径
     */
    private fun startPlayVideo(videoPath: String): Boolean {
        var streamID = 0
        val load = mSoundPool.load(videoPath, DEFAULT_PRIORITY)
        mSoundPool.setOnLoadCompleteListener { soundPool, sampleId, status ->
            streamID =
                mSoundPool.play(load, LEFT_VOLUME.toFloat(), RIGHT_VOLUME.toFloat(), DEFAULT_PRIORITY, LOOP, RATE)
        }
        return streamID != 0
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun makeSound(sound: String?) {
        if (sound == null) return
        val md5 = EncryptUtils.encryptMD5ToString(sound)
        val file = File("$filePath$md5.wav")
        Observable
            .create<Int> {
                if (file.exists() && file.isFile && file.length() < 100) file.delete()
                if (!file.exists()) {
                    val i = textToSpeech?.synthesizeToFile(sound, null, file, md5)
                    it.onNext(i ?: 0)
                }
            }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_STOP)))
            .subscribe {
                if (it != -1) Log.i("HJ", "makeSound sucess path:${file.path}")
                else Log.i("HJ", "makeSound error errorCode:$it")
            }
    }

    fun speak(speeckText: String) {
        stop()
        val file = File("$filePath${EncryptUtils.encryptMD5ToString(speeckText)}.wav")
        if (file.exists()) {
            val playSucess = startPlayVideo(file.path)
            if (!playSucess) textToSpeech?.speak(speeckText, TextToSpeech.QUEUE_FLUSH, null)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) makeSound(speeckText)
            textToSpeech?.speak(speeckText, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    private fun stop() {
        try {
            if (textToSpeech != null && textToSpeech.isSpeaking) textToSpeech.stop()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     *  释放资源
     */
    private fun release() {
        try {
            if (textToSpeech != null) {
                textToSpeech.stop()
                textToSpeech.shutdown()
            }
            mSoundPool.release()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}
