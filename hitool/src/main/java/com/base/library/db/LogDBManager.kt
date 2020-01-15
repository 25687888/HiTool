package com.base.library.db

import android.content.Context

/**
 * Created by admin on 2019-05-13.
 */

object LogDBManager {
    private var helper: LogHelper? = null
    private var logMessageDao: LogMessageDao? = null

    fun init(context: Context) {
        helper = LogHelper.getHelper(context)
        logMessageDao = LogMessageDao(context)
    }

    fun exit() {
        logMessageDao = null
        helper?.close()
    }

    fun add(data: LogMessage) {
        if (logMessageDao != null) {
            LogMessageDao.add(data)
        }

    }

    fun queryAll(): List<LogMessage> {
        return if (logMessageDao != null) {
            LogMessageDao.queryAll()
        } else {
            ArrayList()
        }
    }

    fun queryDate(startDateTime: Long, endDateTime: Long): List<LogMessage> {
        return if (logMessageDao != null) {
            LogMessageDao.queryDate(startDateTime, endDateTime)
        } else {
            ArrayList()
        }
    }

    fun queryDate(startDateTime: Long, endDateTime: Long, offset: Long, limit: Long): List<LogMessage> {
        if (logMessageDao != null) {
            return LogMessageDao.queryDate(startDateTime, endDateTime, offset, limit)
        } else {
            return ArrayList()
        }
    }

    fun queryDateOffset(starttime: Long, offset: Long, limit: Long): List<LogMessage> {
        if (logMessageDao != null) {
            return LogMessageDao.queryDateOffset(starttime, offset, limit)
        } else {
            return ArrayList()
        }
    }

    fun queryDateOffseColumns(starttime: Long, columns: List<String>, offset: Long, limit: Long): List<LogMessage> {
        if (logMessageDao != null) {
            return LogMessageDao.queryDateOffseColumns(starttime, columns, offset, limit)
        } else {
            return ArrayList()
        }
    }

    fun queryDateLimit(startDateTime: Long, count: Int): List<LogMessage> {
        if (logMessageDao != null) {
            return LogMessageDao.query(startDateTime, count.toLong())
        } else {
            return ArrayList()
        }
    }

    fun queryDateAndLevel(startDateTime: Long, endDateTime: Long, level: String): List<LogMessage> {
        if (logMessageDao != null) {
            return LogMessageDao.queryDateAndLevel(startDateTime, endDateTime, level)
        } else {
            return ArrayList()
        }
    }

    fun queryDateAndLevel(
        startDateTime: Long,
        endDateTime: Long,
        level: String,
        offset: Long,
        limit: Long
    ): List<LogMessage> {
        if (logMessageDao != null) {
            return LogMessageDao.queryDateAndLevel(startDateTime, endDateTime, level, offset, limit)
        } else {
            return ArrayList()
        }
    }

    fun deleteDays(days: Int) {
        if (logMessageDao != null) {
            LogMessageDao.deleteDays(days, 5000)
        }
    }

    fun deleteAll() {
        if (logMessageDao != null) {
            LogMessageDao.deleteAll()
        }
    }
}
