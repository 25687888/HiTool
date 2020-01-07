package com.base.library.mvp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.base.library.http.HttpDto
import org.jetbrains.annotations.NotNull

/**
 * 作用: 基于MVP架构的Presenter代理的基类
 */
interface BPresenter: LifecycleObserver {
    /**
     * 数据请求
     * @param http 网络请求封装类
     */
    fun getData(http: HttpDto)

    /**
     * 保存页面或接口数据
     * @param content 保存的内容
     * @param behavior 标记
     * @param level 日志等级
     */
    fun other(content: String, behavior: String, level: String)

    /**
     * Activity 创建
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(@NotNull owner: LifecycleOwner)

    /**
     * Activity 运行
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(@NotNull owner: LifecycleOwner)

    /**
     * Activity 销毁
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(@NotNull owner: LifecycleOwner)

    /**
     * Activity 生命周期改变
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onLifecycleChanged(@NotNull owner: LifecycleOwner, @NotNull event: Lifecycle.Event)
}
