package com.base.library.mvp

import android.annotation.SuppressLint
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.base.library.db.LogDBManager
import com.base.library.db.LogMessage
import com.base.library.http.HttpDto
import com.base.library.util.JsonTool
import com.base.library.util.tryCatch
import com.base.library.view.sweetdialog.BSweetAlertDialog
import com.blankj.utilcode.util.LogUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.exception.HttpException
import com.lzy.okgo.exception.StorageException
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BPresenterImpl<T : BView>(view: T) : BRequestCallback {
    val mView: T? get() = mViewRef.get()
    private var mViewRef = WeakReference(view)//View接口类型的弱引用
    val presenterScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.IO + Job()) }
    private val model: BModel = BModelImpl()
    open var lifecycleOwner: LifecycleOwner? = null

    override fun onCreate(owner: LifecycleOwner) {
        lifecycleOwner = owner
    }

    override fun onResume(owner: LifecycleOwner) {
    }

    override fun onDestroy(owner: LifecycleOwner) {
        OkGo.getInstance().cancelTag(this)
        presenterScope.cancel()
        mViewRef.clear()
    }

    override fun onLifecycleChanged(owner: LifecycleOwner, event: Lifecycle.Event) {
    }

    override fun getData(http: HttpDto) {
        mView?.let {
            http.tag = this
            model.getData(this, http)
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
        LogUtils.e(content)

        /**
         * 不属于静默加载才弹窗
         */
        if (!baseHttpDto.silence) {
            val fl = if (baseHttpDto.isFinish) mView?.getConfirmFinishListener() else null
            mView?.showDialog(BSweetAlertDialog.ERROR_TYPE, "异常提示", content, confirmListener = fl)
        }

        throwable?.printStackTrace()
    }

    override fun other(content: String, behavior: String, level: String) {
        tryCatch({
            if (LogDBManager.isWrite) {
                presenterScope.launch {
                    LogDBManager.add(LogMessage(content, behavior, level))
                }
            }
        })
    }

    /**
     * 异步JsonTool解析,目前只支持对象解析..后续扩展
     */
    inline fun <reified T : Any> asyncJsonTool(
        body: String,
        crossinline successBlock: (T) -> Unit,
        crossinline errorBlock: (Throwable) -> Unit? = { t: Throwable -> t.printStackTrace() }
    ) {
        tryCatch({
            presenterScope.launch {
                val obj = JsonTool.getObject(body, T::class.java)
                withContext(Dispatchers.Main) { successBlock.invoke(obj) }
            }
        }, {
            errorBlock.invoke(it)
        })
    }
}