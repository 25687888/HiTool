package com.base.library.mvp

import com.base.library.http.HttpDto
import com.blankj.utilcode.util.LogUtils
import com.lzy.okgo.model.Response
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class BModelImpl : BModel {

    private var compositeDisposable: CompositeDisposable? = null

    override fun getData(callback: BRequestCallback, http: HttpDto) {
        val requestBody = http.print()
        callback.other(requestBody, "请求参数 ${http.method}", "I")
        http.getOkGo().execute(object : BCallback(callback, http.silence) {
            override fun onSuccess(response: Response<String>?) {
                super.onSuccess(response)
                val body = response?.body() ?: ""
                printLog(http.url, http.method, body)
                callback.other(body, "请求成功 ${http.method}", "I")
                callback.requestSuccess(body, http)
            }

            override fun onError(response: Response<String>?) {
                val throwable = response?.exception
                callback.other("${throwable?.message}", "请求失败 ${http.method}", "E")
                callback.requestError(throwable, http)
                super.onError(response)
            }
        })
    }

    override fun getOkGoRx(callback: BRequestCallback, http: HttpDto) {
        http.getOkGoRx()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { if (!http.silence) callback.beforeRequest() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {
                    addDispose(d)
                }

                override fun onComplete() {
                    callback.requestComplete()
                }

                override fun onNext(s: String) {
                    callback.requestSuccess(s, http)
                }

                override fun onError(e: Throwable) {
                    callback.requestError(e, http)
                }
            })
    }

    override fun addDispose(disposable: Disposable) {
        compositeDisposable ?: let { compositeDisposable = CompositeDisposable() }
        compositeDisposable?.add(disposable)
    }

    override fun closeAllDispose() {
        compositeDisposable?.dispose()
    }

    private fun printLog(url: String, method: String, data: String) {
        LogUtils.i(
            StringBuilder()
                .appendln("Response 地址 : $url")
                .appendln("方法 : $method")
                .appendln("返回数据如下 : ")
                .toString()
        )
        LogUtils.json(data)
    }

}