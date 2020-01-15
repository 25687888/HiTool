package com.base.library.db


import android.annotation.SuppressLint
import android.content.Context
import com.j256.ormlite.dao.Dao
import java.sql.SQLException
import java.util.ArrayList
import java.util.Calendar

class LogMessageDao(context: Context) {
    init {
        try {
            dao = LogHelper.getHelper(context)?.getDao()
        } catch (var3: SQLException) {
            var3.printStackTrace()
        }
    }

    companion object {
        var dao: Dao<LogMessage, Int>? = null

        fun add(data: LogMessage) {
            try {
                dao?.createIfNotExists(data)
            } catch (var2: SQLException) {
                var2.printStackTrace()
            }

        }

        fun update(data: LogMessage) {
            try {
                dao?.createOrUpdate(data)
            } catch (var2: SQLException) {
                var2.printStackTrace()
            }

        }

        fun delete(id: Int): Int {
            try {
                val deleteBuilder = dao!!.deleteBuilder()
                deleteBuilder.where().eq("id", id)
                return deleteBuilder.delete()
            } catch (var2: SQLException) {
                var2.printStackTrace()
                return 0
            }

        }

        fun search(name: String, value: Any): LogMessage? {
            try {
                val datas = dao!!.queryBuilder().where().eq(name, value).query()
                if (datas.size > 0) {
                    return datas[0] as LogMessage
                }
            } catch (var3: SQLException) {
                var3.printStackTrace()
            }

            return null
        }

        fun search(id: Int): LogMessage? {
            try {
                val datas = dao!!.queryBuilder().where().eq("id", id).query()
                if (datas.size > 0) {
                    return datas[0] as LogMessage
                }
            } catch (var2: SQLException) {
                var2.printStackTrace()
            }

            return null
        }

        fun searchAll(id: Int): List<LogMessage> {
            try {
                return dao!!.queryBuilder().where().eq("id", id).query()
            } catch (var2: SQLException) {
                var2.printStackTrace()
                return ArrayList()
            }

        }

        fun searchAll(name: String, value: Any): List<LogMessage> {
            try {
                return dao!!.queryBuilder().where().eq(name, value).query()
            } catch (var3: SQLException) {
                var3.printStackTrace()
                return ArrayList()
            }

        }

        fun deleteAll() {
            try {
                dao!!.delete(queryAll())
            } catch (var1: SQLException) {
                var1.printStackTrace()
            }

        }

        fun queryAll(): List<LogMessage> {
            var users = ArrayList<LogMessage>()

            try {
                users = dao!!.queryForAll() as ArrayList<LogMessage>
            } catch (var2: SQLException) {
                var2.printStackTrace()
            }

            return users
        }

        fun query(offset: Long, limit: Long): List<LogMessage> {
            var users = ArrayList<LogMessage>()
            try {
                users = dao!!.queryBuilder().offset(offset).limit(limit).query() as ArrayList<LogMessage>
            } catch (var6: SQLException) {
                var6.printStackTrace()
            }

            return users
        }

        fun queryDate(starttime: Long, end: Long): List<LogMessage> {
            var users = ArrayList<LogMessage>()
            try {
                users = dao!!.queryBuilder().orderBy("id", false).where().between(
                    "time", starttime, end).query() as ArrayList<LogMessage>
            } catch (var6: SQLException) {
                var6.printStackTrace()
            }

            return users
        }

        fun queryDate(starttime: Long, end: Long, offset: Long, limit: Long): List<LogMessage> {
            var users = ArrayList<LogMessage>()
            try {
                users = dao!!.queryBuilder().orderBy("id", false).offset(offset).limit(limit).where()
                    .between("time", starttime, end).query() as ArrayList<LogMessage>
            } catch (var10: SQLException) {
                var10.printStackTrace()
            }

            return users
        }

        fun queryDateOffset(starttime: Long, offset: Long, limit: Long): List<LogMessage> {
            var users = ArrayList<LogMessage>()
            try {
                users = dao!!.queryBuilder().offset(offset).limit(limit).where().gt(
                    "time",
                    starttime
                ).query() as ArrayList<LogMessage>
            } catch (var8: SQLException) {
                var8.printStackTrace()
            }

            return users
        }

        fun queryDateOffseColumns(starttime: Long, columns: List<String>, offset: Long, limit: Long): List<LogMessage> {
            var users = ArrayList<LogMessage>()
            try {
                users = dao!!.queryBuilder().selectColumns(columns).offset(offset).limit(limit).where()
                    .gt("time", starttime).query() as ArrayList<LogMessage>
            } catch (var9: SQLException) {
                var9.printStackTrace()
            }

            return users
        }

        fun queryDateAndLevel(start: Long, end: Long, level: String, offset: Long, limit: Long): List<LogMessage> {
            var users = ArrayList<LogMessage>()

            try {
                users = dao!!.queryBuilder().orderBy("id", false).offset(offset).limit(limit).where()
                    .between("time", start, end).and().eq("level", level).query() as ArrayList<LogMessage>
            } catch (var11: SQLException) {
                var11.printStackTrace()
            }

            return users
        }

        fun queryDateAndLevel(start: Long, end: Long, level: String): List<LogMessage> {
            var users = ArrayList<LogMessage>()

            try {
                users = dao!!.queryBuilder().orderBy("id", false).where().between("time", start, end).and()
                    .eq("level", level).query() as ArrayList<LogMessage>
            } catch (var7: SQLException) {
                var7.printStackTrace()
            }

            return users
        }

        @SuppressLint("WrongConstant")
        fun deleteDays(days: Int, leftCount: Int) {
            val calendar = Calendar.getInstance()
            if (days != 0) {
                calendar.add(5, days)
            }
            try {
                val size = dao!!.queryBuilder().countOf()
                if (size < (leftCount + 1).toLong()) {
                    return
                }

                val lastMessage = dao!!.queryBuilder().orderBy("id", false).queryForFirst() as LogMessage
                if (null != lastMessage) {
                    val user = dao!!.queryBuilder().orderBy("id", false).where().lt(
                        "time",
                        calendar.timeInMillis
                    ).and().lt("id", lastMessage.id - leftCount).queryForFirst()
                    if (null != user) {
                        val deleteBuilder = dao!!.deleteBuilder()
                        deleteBuilder.where().lt("id", user.id)
                        deleteBuilder.delete()
                    }
                }
            } catch (var8: SQLException) {
                var8.printStackTrace()
            }
        }

        fun queryLt(time: Long): List<LogMessage> {
            var users = ArrayList<LogMessage>()
            try {
                users =
                    dao!!.queryBuilder().orderBy("id", false).where().lt("time", time).query() as ArrayList<LogMessage>
            } catch (var4: SQLException) {
                var4.printStackTrace()
            }
            return users
        }
    }
}
