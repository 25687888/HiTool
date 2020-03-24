package com.base.library.db

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.blankj.utilcode.util.SDCardUtils
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import java.io.File
import java.sql.SQLException
import java.util.HashMap

/**
 * Created by admin on 2019-05-13.
 */

class LogHelper(context: Context) : OrmLiteSqliteOpenHelper(context, tableName, null, 1) {

    @Synchronized
    override fun getWritableDatabase(): SQLiteDatabase {
        return SQLiteDatabase.openDatabase(pathName + tableName, null, 0)
    }

    @SuppressLint("WrongConstant")
    @Synchronized
    override fun getReadableDatabase(): SQLiteDatabase {
        return SQLiteDatabase.openDatabase(pathName + tableName, null, 1)
    }

    override fun onCreate(database: SQLiteDatabase, connectionSource: ConnectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, LogMessage::class.java)
        } catch (var4: SQLException) {
            var4.printStackTrace()
        }
    }

    override fun onUpgrade(
        database: SQLiteDatabase,
        connectionSource: ConnectionSource,
        oldVersion: Int,
        newVersion: Int
    ) {
        try {
            if (oldVersion < VERSION) {
                TableUtils.dropTable<LogMessage, Any>(connectionSource, LogMessage::class.java, true)
                TableUtils.createTable(connectionSource, LogMessage::class.java)
            }
        } catch (var6: SQLException) {
            var6.printStackTrace()
        }
    }

    @Synchronized
    inline fun <reified T : Any> getDao(): Dao<T, Int>? {
        var dao: Dao<T, Int>? = null
        val className = T::class.java.simpleName
        if (mapDao.containsKey(className)) {
            dao = mapDao[className] as Dao<T, Int>?
        }
        if (dao == null) {
            dao = super.getDao(T::class.java)
            mapDao[className] = dao
        }
        return dao
    }

    companion object {
        val mapDao = HashMap<String, Any>()
        private const val VERSION = 1
        var instance: LogHelper? = null
        const val tableName = "log.db"
        val pathName: String = "${SDCardUtils.getSDCardInfo()[0].path}/sendInfo/db/"
        @Synchronized
        fun getHelper(context: Context): LogHelper? {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        val path = File(pathName)
                        if (!path.exists()) path.mkdirs()
                        val db = SQLiteDatabase.openOrCreateDatabase(pathName + tableName, null)
                        instance = LogHelper(context.applicationContext)
                        instance?.onCreate(db)
                        db.close()
                    }
                }
            }
            return instance
        }
    }
}
