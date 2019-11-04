package com.base.library.mvp

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.base.library.http.HttpDto
import com.base.library.view.AlertDialog
import com.lzy.okgo.OkGo
import com.lzy.okgo.exception.HttpException
import com.lzy.okgo.exception.StorageException
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import talex.zsw.basecore.util.JsonTool
import talex.zsw.basecore.util.LogTool
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BPresenterImpl<T : BView>(var mView: T) : BPresenter, BRequestCallback {

    private val model: BModel = BModelImpl()
    open var lifecycleOwner: LifecycleOwner? = null

    override fun onCreate(owner: LifecycleOwner) {
        lifecycleOwner = owner
    }

    override fun onResume(owner: LifecycleOwner) {
    }

    override fun onDestroy(owner: LifecycleOwner) {
        OkGo.getInstance().cancelTag(this)
        model.closeAllDispose()
    }

    override fun onLifecycleChanged(owner: LifecycleOwner, event: Lifecycle.Event) {
    }

    override fun getData(http: HttpDto) {
        mView?.let {
            http.tag = this
            when (http.httpMode) {
                HttpDto.getOkGo -> model.getData(this, http)
                HttpDto.getOkGoRx -> model.getOkGoRx(this, http)
            }
        }
    }

    override fun beforeRequest() {
        mView?.showDialog()
    }

    override fun requestComplete() {}

    @SuppressLint("CheckResult")
    override fun requestSuccess(body: String, bHttpDto: HttpDto) {
        mView?.disDialog()

    }

    override fun requestError(throwable: Throwable?, baseHttpDto: HttpDto) {
        mView?.disDialog()

        var content = "额...出错了"
        if (throwable is UnknownHostException || throwable is ConnectException) {
            content = "网络连接失败,请连接网络"
        } else if (throwable is SocketTimeoutException) {
            content = "网络请求超时"
        } else if (throwable is HttpException) {
            content = "响应码404和500,服务器内部错误"
        } else if (throwable is StorageException) {
            content = "SD卡不存在或者没有权限"
        } else if (throwable is IllegalStateException) {
            content = throwable.message ?: "额...出错了"
        }
        LogTool.e(content)

        /**
         * 不属于静默加载才弹窗
         */
        if (!baseHttpDto.silence) {
            val fl = if (baseHttpDto.isFinish) mView?.getConfirmFinishListener() else null
            mView?.showDialog(AlertDialog.ERROR_TYPE, "异常提示", content, confirmListener = fl)
        }

        throwable?.printStackTrace()
    }

    override fun other(content: String, behavior: String, level: String) {
        mView?.other(content, behavior, level)
    }

    /**
     * 异步JsonTool解析,目前只支持对象解析..后续扩展
     */
    inline fun <reified T : Any> asyncJsonTool(
        body: String,
        crossinline successBlock: (T) -> Unit,
        crossinline errorBlock: (Throwable) -> Unit? = { t: Throwable -> t.printStackTrace() }
    ) {
        Observable
            .just(body)
            .subscribeOn(Schedulers.io())
            .map { JsonTool.getObject(it, T::class.java) }
            .observeOn(AndroidSchedulers.mainThread())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner)))
            .subscribe({ successBlock(it) }, { errorBlock(it) })
    }
}