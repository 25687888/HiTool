package com.base.library.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.base.library.database.entity.JournalRecord

@Dao
interface JournalRecordDao {

    @Insert
    suspend fun insertCts(journalRecord: JournalRecord): Long

    @Query("delete from JournalRecord where time <= :timeJs")
    suspend fun deleteFormTimeCts(timeJs: String): Int

    //根据时间段查询数据
    @Query("select * from JournalRecord where time >= :startTime and time <= :endTime")
    suspend fun queryFormTimeCts(startTime: String, endTime: String): List<JournalRecord>
}
