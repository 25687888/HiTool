package com.base.library.db

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.TimeUtils
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.io.Serializable
import java.text.SimpleDateFormat

/**
 * Created by admin on 2019-05-13.
 */
@DatabaseTable(tableName = "LogMessage")
class LogMessage : Serializable {
    @DatabaseField(generatedId = true, columnName = "id")// 设置主键并且自动增长
    var id: Int = 0

    @DatabaseField(columnName = "content")
    var content: String = ""// 内容

    @DatabaseField(columnName = "behavior")
    var behavior: String = "" // 行为

    @DatabaseField(columnName = "time")
    var time: Long = TimeUtils.getNowDate().time // 时间

    @DatabaseField(columnName = "level")
    var level: String = "I" // 等级

    @DatabaseField(columnName = "packageName")
    var packageName: String = AppUtils.getAppPackageName() // 包名

    @DatabaseField(columnName = "version")
    var version: String = AppUtils.getAppVersionName() // 版本

    constructor(content: String, behavior: String, level: String) {
        this.content = content
        this.behavior = behavior
        this.level = level
    }

    constructor()
}
