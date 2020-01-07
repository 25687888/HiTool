package com.base.library.util.transferfile

import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.LogUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Progress
import com.lzy.okserver.OkUpload
import com.lzy.okserver.upload.UploadListener
import com.base.library.util.transferfile.entitys.FileTransfer

import java.io.File

/**
 * 文件上传工具类
 */

class UpLoadTool : BTransfer {
    init {
        OkUpload.getInstance().threadPool.setCorePoolSize(1)
    }

    override fun transferFile(fileTransfer: FileTransfer, listener: (FileTransfer) -> Unit) {
        val taskTag = EncryptUtils.encryptMD5ToString(fileTransfer.url)
        OkUpload
            .request<String>(taskTag, OkGo.post<String>(fileTransfer.url).params("File", File(fileTransfer.fileName)))
            .priority(100)
            .extra1(fileTransfer)
            .save()
            .register(object : UploadListener<String>(taskTag) {
                override fun onStart(progress: Progress) {
                    fileTransfer.process = progress
                    listener.invoke(fileTransfer)
                }

                override fun onProgress(progress: Progress) {
                    if (progress.status == Progress.ERROR) return
                    if (progress.status == Progress.FINISH) return
                    fileTransfer.process = progress
                    listener.invoke(fileTransfer)
                }

                override fun onError(progress: Progress) {
                    OkUpload.getInstance().getTask(progress.tag).start()//当下载出错是重新开始下载
                }

                override fun onFinish(file: String, progress: Progress) {
                    fileTransfer.process = progress
                    listener.invoke(fileTransfer)
                    OkUpload.getInstance().getTask(progress.tag).remove()
                }

                override fun onRemove(progress: Progress) {
                    LogUtils.i("UpLoadTool onRemove $progress")
                }

            }).start()
    }
}
