package es.sdos.library.androidextensions

import android.content.SharedPreferences
import java.io.InvalidClassException

inline fun <reified T> SharedPreferences.get(key: String, defaultValue: T): T {
    return when(T::class) {
        Boolean::class -> this.getBoolean(key, defaultValue as Boolean) as T
        Float::class -> this.getFloat(key, defaultValue as Float) as T
        Int::class -> this.getInt(key, defaultValue as Int) as T
        Long::class -> this.getLong(key, defaultValue as Long) as T
        String::class -> this.getString(key, defaultValue as String) as T
        else -> throw InvalidClassException("Only Boolean, Float, Int, Long or String are valid")
    }
}

inline fun <reified T> SharedPreferences.put(key: String, value: T) {
    val editor = this.edit()
    when(T::class) {
        Boolean::class -> editor.putBoolean(key, value as Boolean)
        Float::class -> editor.putFloat(key, value as Float)
        Int::class -> editor.putInt(key, value as Int)
        Long::class -> editor.putLong(key, value as Long)
        String::class -> editor.putString(key, value as String)
        else -> throw InvalidClassException("Only Boolean, Float, Int, Long or String are valid")
    }
    editor.apply()
}