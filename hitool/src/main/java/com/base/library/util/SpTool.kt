package com.base.library.util

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.tencent.mmkv.MMKV

/**
 * MMKV 取代原生SharedPreferences
 * SharedPreferences的常规使用
 */
object SpTool {
    private lateinit var prefs: MMKV

    /**
     * 获取所有键值对
     */
    val all: Map<String, *> get() = prefs?.all

    /**
     * 单进程存储数据
     */
    fun init(context: Context) {
        MMKV.initialize(context)
        prefs = MMKV.mmkvWithID(context.packageName)
    }


    /**
     * 多进程共享数据
     */
    fun initMulitProcess(context: Context) {
        MMKV.initialize(context)
        prefs = MMKV.mmkvWithID(context.packageName, MMKV.MULTI_PROCESS_MODE)
    }

    fun getString(TAG: String, def: String): String {
        return prefs?.getString(TAG, def)?: ""
    }

    fun getString(TAG: String): String {
        return prefs?.getString(TAG, "") ?: ""
    }

    fun saveString(TAG: String, data: String) {
        prefs?.edit().putString(TAG, data)
    }

    fun saveObject(TAG: String, obj: Any?) {
        val editor = prefs?.edit()
        if (obj != null) {
            editor.putString(TAG, JsonTool.getJsonString(obj))
        } else {
            editor.putString(TAG, "")
        }
    }

    fun <T> getObject(TAG: String, t: Class<T>): T? {
        val str: String = prefs.getString(TAG, "") ?: ""
        return if (TextUtils.isEmpty(str)) null else JsonTool.getObject(str, t)
    }

    fun getBoolean(TAG: String, def: Boolean): Boolean {
        return prefs?.getBoolean(TAG, def)
    }

    fun getBoolean(TAG: String): Boolean {
        return prefs?.getBoolean(TAG, false)
    }

    fun saveBoolean(TAG: String, flag: Boolean) {
        prefs?.edit().putBoolean(TAG, flag)
    }

    fun getInt(TAG: String, def: Int): Int {
        return prefs?.getInt(TAG, def)
    }

    fun getInt(TAG: String): Int {
        return prefs?.getInt(TAG, -1)
    }

    fun saveInt(TAG: String, data: Int) {
        prefs?.edit().putInt(TAG, data)
    }

    fun getLong(TAG: String, def: Long): Long {
        return prefs?.getLong(TAG, def)
    }

    fun getLong(TAG: String): Long {
        return prefs?.getLong(TAG, -1)
    }

    fun saveLong(TAG: String, data: Long) {
        prefs?.edit().putLong(TAG, data)
    }

    fun getFloat(TAG: String, def: Float): Float {
        return prefs?.getFloat(TAG, def)
    }

    fun getFloat(TAG: String): Float {
        return prefs?.getFloat(TAG, -1f)
    }

    fun saveFloat(TAG: String, data: Float) {
        prefs?.edit().putFloat(TAG, data)
    }

    /**
     * 是否存在该 key
     */
    operator fun contains(key: String): Boolean {
        return prefs?.contains(key)
    }

    /**
     * 移除该 key
     */
    @JvmOverloads
    fun remove(key: String, isCommit: Boolean = false) {
        if (isCommit) {
            prefs?.edit().remove(key).commit()
        } else {
            prefs?.edit().remove(key)
        }
    }

    /**
     * 清除所有数据
     */
    @JvmOverloads
    fun clear(isCommit: Boolean = false) {
        if (isCommit) {
            prefs?.edit().clear().commit()
        } else {
            prefs?.edit().clear()
        }
    }
}
/**
 * 移除该 key
 */
/**
 * 清除所有数据
 */
