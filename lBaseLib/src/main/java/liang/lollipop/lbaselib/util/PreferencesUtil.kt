package liang.lollipop.lbaselib.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by lollipop on 2018/1/11.
 * @author Lollipop
 * 偏好设置相关的工具类
 */
object PreferencesUtil {

    fun <T> put(context: Context, key: String, value: T) {
        val mShareConfig = PreferenceManager.getDefaultSharedPreferences(context)
        put(mShareConfig, key, value)
    }

    private fun <T> put(mShareConfig: SharedPreferences, key: String, value: T) {
        val conEdit = mShareConfig.edit()
        when (value) {
            is String -> conEdit.putString(key, (value as String).trim())
            is Long -> conEdit.putLong(key, value as Long)
            is Boolean -> conEdit.putBoolean(key, value as Boolean)
            is Int -> conEdit.putInt(key, value as Int)
            is Float -> conEdit.putFloat(key, value as Float)
        }
        conEdit.apply()
    }

    inline fun <reified T> get(context: Context, key: String, defValue: T): T {
        val mShareConfig = PreferenceManager.getDefaultSharedPreferences(context)
        var value: T? = null
        when (defValue) {
            is String -> value = mShareConfig.getString(key, defValue as String) as T
            is Long -> value = java.lang.Long.valueOf(mShareConfig.getLong(key, defValue as Long)) as T
            is Boolean -> value = java.lang.Boolean.valueOf(mShareConfig.getBoolean(key, defValue as Boolean)) as T
            is Int -> value = Integer.valueOf(mShareConfig.getInt(key, defValue as Int)) as T
            is Float -> value = java.lang.Float.valueOf(mShareConfig.getFloat(key, defValue as Float)) as T
        }
        return value?:defValue
    }

}