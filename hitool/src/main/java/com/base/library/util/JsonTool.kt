package com.base.library.util

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.LongSerializationPolicy
import com.google.gson.reflect.TypeToken

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.lang.reflect.Type
import java.math.BigDecimal
import java.util.ArrayList
import java.util.HashMap
import java.util.TreeMap

/**
 * 作用: Json解析,拼装类
 */
object JsonTool {
    private val gson: Gson

    init {
        val gb = GsonBuilder()
        gb.setLongSerializationPolicy(LongSerializationPolicy.STRING)
        gb.registerTypeAdapter(Double::class.java, JsonSerializer<Double> { originalValue, _, _ ->
            val bigValue = BigDecimal.valueOf(originalValue)
            if (originalValue == bigValue.toDouble()) {
                JsonPrimitive(bigValue.toInt())
            } else {
                JsonPrimitive(originalValue)
            }
        })
        gb.registerTypeAdapter(Long::class.java, JsonSerializer<Long> { originalValue, _, _ ->
            val bigValue = BigDecimal.valueOf(originalValue)
            JsonPrimitive(bigValue.toPlainString())
        })
        gb.registerTypeAdapter(Int::class.java, JsonSerializer<Int> { originalValue, _, _ ->
            val bigValue = BigDecimal.valueOf(originalValue.toLong())
            JsonPrimitive(bigValue.toPlainString())
        })
        gb.registerTypeAdapter(
            object : TypeToken<TreeMap<String, Any>>() {}.type,
            JsonDeserializer { json, _, _ ->
                val treeMap = TreeMap<String, Any>()
                val jsonObject = json.asJsonObject
                val entrySet = jsonObject.entrySet()
                for ((key, value) in entrySet) treeMap[key] = value
                treeMap
            })
        gson = gb.create()
    }

    /**
     * 将json解析成指定泛型并返回
     *
     * @param string json数据
     * @param <T>    指定泛型
    </T> */
    fun <T> getObject(string: String, t: Class<T>): T {
        return gson.fromJson(string, t)
    }

    /**
     * 将object解析成指定泛型并返回
     *
     * @param obj json数据的object
     * @param <T> 指定泛型
    </T> */
    fun <T> getObject(obj: Any, t: Class<T>): T {
        val gson = gson
        var data = gson.toJson(obj)
        if (TextUtils.isEmpty(data)) {
            data = ""
        }
        return gson.fromJson(data, t)
    }

    /**
     * 将object解析成指定泛型并返回
     * TypeToken token =  new TypeToken<ArrayList></ArrayList><BannerVo>>(){};
     * val list = JsonTool.getObject(response.data, object : TypeToken<ArrayList></ArrayList><SearchOrderVo>>(){})
     *
     * @param obj   json数据的object
     * @param token 解析类型token
    </SearchOrderVo></BannerVo> */
    fun getObject(obj: Any, token: TypeToken<*>): Any? {
        val gson = gson
        var data = gson.toJson(obj)
        if (TextUtils.isEmpty(data)) {
            data = ""
        }
        return gson.fromJson<Any>(data, token.type)
    }

    /**
     * 将object解析成指定泛型并返回
     * TypeToken token =  new TypeToken<ArrayList></ArrayList><BannerVo>>(){};
     * val list = JsonTool.getObject(response.data, object : TypeToken<ArrayList></ArrayList><SearchOrderVo>>(){})
     *
     * @param string json数据
     * @param token  解析类型token
    </SearchOrderVo></BannerVo> */
    fun getObject(string: String, token: TypeToken<*>): Any? {
        var string = string
        val gson = gson
        if (TextUtils.isEmpty(string)) {
            string = ""
        }
        return gson.fromJson<Any>(string, token.type)
    }

    /**
     * 将指定类变成Json型数据返回
     *
     * @param obj 指定类型
     * @param <T> 指定泛型
    </T> */
    fun <T> getJsonString(obj: T): String {
        val gson = gson
        var data = gson.toJson(obj)
        if (TextUtils.isEmpty(data)) {
            data = ""
        }
        return data
    }

    /**
     * 将json字符串解析成Map
     *
     * @param jsonStr json数据的object
     */
    fun getMapFromJson(jsonStr: String): Map<String, String>? {
        if (TextUtils.isEmpty(jsonStr)) {
            return null
        }
        val jsonObject: JSONObject
        try {
            jsonObject = JSONObject(jsonStr)

            val keyIter = jsonObject.keys()
            var key: String
            var value: String
            val valueMap = HashMap<String, String>()
            while (keyIter.hasNext()) {
                key = keyIter.next()
                value = jsonObject.get(key).toString()
                if (!TextUtils.isEmpty(value)) {
                    valueMap[key] = value
                }
            }
            return valueMap
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 将object解析成Map
     *
     * @param obj 数据object
     */
    fun getMapFromObj(obj: Any): Map<String, String>? {
        val jsonStr = getJsonString(obj)
        return getMapFromJson(jsonStr)
    }

    /**
     * 将json 数组转换为Map 对象
     *
     * @param jsonString
     * @return
     */
    fun getMap(jsonString: String): Map<String, Any>? {
        val jsonObject: JSONObject
        try {
            jsonObject = JSONObject(jsonString)
            val keyIter = jsonObject.keys()
            var key: String
            var value: Any
            val valueMap = HashMap<String, Any>()
            while (keyIter.hasNext()) {
                key = keyIter.next() as String
                value = jsonObject.get(key)
                valueMap[key] = value
            }
            return valueMap
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 将json 数组转换为Map 对象
     *
     * @param obj
     * @return
     */
    fun getMap(obj: Any): Map<String, Any>? {
        val jsonStr = getJsonString(obj)
        return getMap(jsonStr)
    }
}
