package com.base.library.util.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.SimpleTarget
import java.util.concurrent.ExecutionException

/**
 * 作用: GlideTool
 */
object GlideTool {

    /**
     * 加载图片
     *
     * @param v   ImageView
     * @param uri 图片路径 本地图片或Drawble图片或者网络图片
     */
    fun loadImg(v: ImageView, uri: Any) {
        LogUtils.i("IMG", uri)
        GlideApp
            .with(v.context)
            .load(uri)
            .transition(DrawableTransitionOptions.withCrossFade())
            .fitCenter()
            .into(v)
    }

    /**
     * 加载图片,设置占位图
     *
     * @param v      ImageView
     * @param url    图片URL
     * @param holder 占位图
     */
    fun loadImg(v: ImageView, url: String, holder: Int) {
        LogUtils.i("IMG", url)
        GlideApp
            .with(v.context)
            .load(url)
            .placeholder(holder)
            .error(holder)
            .fallback(holder)
            .transition(DrawableTransitionOptions.withCrossFade())
            .fitCenter()
            .into(v)
    }

    /**
     * 加载图片,设置占位图
     *
     * @param v      ImageView
     * @param url    图片URL
     * @param holder 占位图
     */
    fun loadImg(v: ImageView, url: String, holder: Drawable) {
        LogUtils.i("IMG", url)
        GlideApp
            .with(v.context)
            .load(url)
            .placeholder(holder)
            .error(holder)
            .fallback(holder)
            .transition(DrawableTransitionOptions.withCrossFade())
            .fitCenter()
            .into(v)
    }

    /**
     * 加载图片,重新调整图片大小
     *
     * @param v      ImageView
     * @param url    图片URL
     * @param width  图片的宽
     * @param height 图片的高
     */
    fun loadImg(v: ImageView, url: String, width: Int, height: Int) {
        LogUtils.i("IMG", url)
        GlideApp
            .with(v.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .override(width, height)
            .into(v)
    }

    /**
     * 加载图片,为图片加入一些变化
     *
     * @param v              ImageView
     * @param url            图片URL
     * @param transformation 变化
     */
    fun loadImg(v: ImageView, url: String, transformation: Transformation<Bitmap>) {
        LogUtils.i("IMG", url)
        GlideApp
            .with(v.context)
            .asBitmap()
            .load(url)
            .transition(BitmapTransitionOptions.withCrossFade())
            .transform(transformation)
            .into(v)
    }

    /**
     * 图片可能不完整
     * 缩放宽和高都到达View的边界，有一个参数在边界上，另一个参数可能在边界上，也可能超过边界
     *
     * @param v   ImageView
     * @param url 图片URL
     */
    fun loadImgCenterCrop(v: ImageView, url: String) {
        LogUtils.i("IMG", url)
        GlideApp
            .with(v.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .into(v)
    }

    /**
     * 图片完整
     * 如果宽和高都在View的边界内，那就不缩放，否则缩放宽和高都进入View的边界，有一个参数在边界上，另一个参数可能在边界上，也可能在边界内
     *
     * @param v   ImageView
     * @param url 图片URL
     */
    fun loadImgCenterInside(v: ImageView, url: String) {
        LogUtils.i("IMG", url)
        GlideApp
            .with(v.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerInside()
            .into(v)
    }

    /**
     * 图片完整
     * 缩放宽和高都进入View的边界，有一个参数在边界上，另一个参数可能在边界上，也可能在边界内
     *
     * @param v   ImageView
     * @param url 图片URL
     */
    fun loadImgFitCenter(v: ImageView, url: String) {
        LogUtils.i("IMG", url)
        GlideApp
            .with(v.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .fitCenter()
            .into(v)
    }

    /**
     * 加载圆形图片
     *
     * @param v   ImageView
     * @param url 图片URL
     */
    fun loadImgCircleCrop(v: ImageView, url: String) {
        LogUtils.i("IMG", url)
        GlideApp
            .with(v.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .circleCrop()
            .into(v)
    }

    /**
     * 加载圆形图片
     *
     * @param v      ImageView
     * @param url    图片URL
     * @param holder 占位图
     */
    fun loadImgCircleCrop(v: ImageView, url: String, holder: Int) {
        LogUtils.i("IMG", url)
        GlideApp
            .with(v.context)
            .load(url)
            .placeholder(holder)
            .error(holder)
            .fallback(holder)
            .transition(DrawableTransitionOptions.withCrossFade())
            .circleCrop()
            .into(v)
    }

    /**
     * 加载圆形图片
     *
     * @param v      ImageView
     * @param url    图片URL
     * @param holder 占位图
     */
    fun loadImgCircleCrop(v: ImageView, url: String, holder: Drawable) {
        LogUtils.i("IMG", url)
        GlideApp
            .with(v.context)
            .load(url)
            .placeholder(holder)
            .error(holder)
            .fallback(holder)
            .transition(DrawableTransitionOptions.withCrossFade())
            .circleCrop()
            .into(v)
    }

    /**
     * 加载圆角图片
     *
     * @param v   ImageView
     * @param url 图片URL
     */
    fun loadImgRoundedCorners(v: ImageView, url: String) {
        LogUtils.i("IMG", url)
        GlideApp
            .with(v.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .transform(RoundedCorners(11))
            .into(v)
    }

    /**
     * 加载圆角图片
     *
     * @param v              ImageView
     * @param url            图片URL
     * @param roundingRadius 元角度数(px)
     */
    fun loadImgRoundedCorners(v: ImageView, url: String, roundingRadius: Int) {
        LogUtils.i("IMG", url)
        GlideApp
            .with(v.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .transform(RoundedCorners(roundingRadius))
            .into(v)
    }

    /**
     * 加载图片并且不缓存
     *
     * @param v   ImageView
     * @param url 图片URL
     */
    fun loadImgNoCache(v: ImageView, url: String) {
        LogUtils.i("IMG", url)
        GlideApp
            .with(v.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(v)
    }

    /**
     * 加载图片返回Bitmap
     *
     * @param context 上下文
     * @param url     图片URL
     * @param target  目标
     */
    fun loadImageSimpleTarget(context: Context, url: String, target: SimpleTarget<Bitmap>) {
        GlideApp
            .with(context)
            .asBitmap()
            .load(url)
            .transition(BitmapTransitionOptions.withCrossFade())
            .into(target)
    }

    fun getBitmap(url: String): Bitmap? {
        LogUtils.i("IMG", url)
        var bitmap: Bitmap? = null
        try {
            bitmap = GlideApp.with(Utils.getApp())
                .asBitmap()
                .load(url)
                .centerCrop()
                .into(500, 500)
                .get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

        return bitmap
    }

    fun getBitmapPath(url: String): String {
        LogUtils.i("IMG", url)
        var path = ""
        val future = GlideApp.with(Utils.getApp()).load(url).downloadOnly(500, 500)
        try {
            val cacheFile = future.get()
            path = cacheFile.absolutePath
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

        return path
    }

    /**
     * 清空图片缓存
     */
    fun clean() {
        //磁盘缓存清理（子线程）
        GlideApp.get(Utils.getApp()).clearDiskCache()
        //内存缓存清理（主线程）
        GlideApp.get(Utils.getApp()).clearMemory()
    }
}
