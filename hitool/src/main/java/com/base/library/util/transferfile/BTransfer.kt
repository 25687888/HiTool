package com.base.library.util.transferfile

import com.base.library.util.transferfile.entitys.FileTransfer

/**
 * 逻辑调用接口
 */
interface BTransfer {
    fun transferFile(fileTransfer: FileTransfer, listener: (FileTransfer) -> Unit)
}
