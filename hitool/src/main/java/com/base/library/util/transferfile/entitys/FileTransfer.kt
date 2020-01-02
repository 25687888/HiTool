package com.base.library.util.transferfile.entitys

import com.lzy.okgo.model.Progress
import java.io.Serializable

/**
 * 文件下载或上传
 * @param type 1下载apk 2 下载OTA 3上传文件 4下载文件
 * @param file 若下载，文件名称 ；若上传，文件磁盘绝对路径
 * @param url  若下载，下载URI ；若上传，上传URI
 * @param process  下载或上传进度回调
 */
data class FileTransfer(var type: Int = Apk, var fileName: String, var url: String, var process: Progress? = null) : Serializable {
    companion object {
        val Apk = 1
        val Ota = 2
        val UploadFile = 3
        val DownloadFile = 4
    }
}