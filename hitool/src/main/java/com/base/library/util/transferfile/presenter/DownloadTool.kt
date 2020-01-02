package com.base.library.util.transferfile.presenter

import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.LogUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Progress
import com.lzy.okserver.OkDownload
import com.lzy.okserver.download.DownloadListener
import com.base.library.util.transferfile.entitys.FileTransfer
import java.io.File

/**
 * 文件下载工具类
 * @param folder 文件存储文件夹路径
 */
class DownloadTool(var folder: String) : BTransfer {

    override fun transferFile(fileTransfer: FileTransfer, listener: (FileTransfer) -> Unit) {
        val taskTag = EncryptUtils.encryptMD5ToString(fileTransfer.url)
        OkDownload.getInstance().folder = folder
        OkDownload.getInstance().threadPool.setCorePoolSize(3)
        OkDownload
            .request(taskTag, OkGo.get<File>(fileTransfer.url))
            .fileName(fileTransfer.fileName)
            .folder(folder)
            .priority(100)
            .extra1(fileTransfer)
            .save()
            .register(object : DownloadListener(taskTag) {
                override fun onStart(progress: Progress) {
                    fileTransfer.process = progress
                    listener.invoke(fileTransfer)
                }

                override fun onProgress(progress: Progress) {
                    if (progress.status == Progress.ERROR) return
                    fileTransfer.process = progress
                    listener.invoke(fileTransfer)
                }

                override fun onError(progress: Progress) {
                    OkDownload.getInstance().getTask(progress.tag).start()//当下载出错是重新开始下载
                }

                override fun onFinish(file: File, progress: Progress) {
                    LogUtils.i("DownloadTool onFinish  ${file.path}")
                    fileTransfer.process = progress
                    listener.invoke(fileTransfer)
                    OkDownload.getInstance().getTask(progress.tag).remove(false)
                }

                override fun onRemove(progress: Progress) {
                }
            }).start()
    }
}
