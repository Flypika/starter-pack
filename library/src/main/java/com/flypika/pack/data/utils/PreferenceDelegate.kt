package com.flypika.pack.data.utils

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author Timur Seisembayev
 * @since 02.02.2019
 */

fun SharedPreferences.string(
    key: (KProperty<*>) -> String = KProperty<*>::name,
    defaultValue: String = ""
): ReadWriteProperty<Any, String?> =
    object : ReadWriteProperty<Any, String?> {
        override fun getValue(thisRef: Any, property: KProperty<*>) =
            getString(key(property), defaultValue)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) =
            edit().putString(key(property), value).apply()
    }

fun SharedPreferences.boolean(
    key: (KProperty<*>) -> String = KProperty<*>::name,
    defaultValue: Boolean = false
): ReadWriteProperty<Any, Boolean> =
    object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>) =
            getBoolean(key(property), defaultValue)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) =
            edit().putBoolean(key(property), value).apply()
    }

fun SharedPreferences.int(
    key: (KProperty<*>) -> String = KProperty<*>::name,
    defaultValue: Int = 0
): ReadWriteProperty<Any, Int> =
    object : ReadWriteProperty<Any, Int> {
        override fun getValue(thisRef: Any, property: KProperty<*>) =
            getInt(key(property), defaultValue)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) =
            edit().putInt(key(property), value).apply()
    }

fun SharedPreferences.long(
    key: (KProperty<*>) -> String = KProperty<*>::name,
    defaultValue: Long = 0.toLong()
): ReadWriteProperty<Any, Long> =
    object : ReadWriteProperty<Any, Long> {
        override fun getValue(thisRef: Any, property: KProperty<*>) =
            getLong(key(property), defaultValue)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) =
            edit().putLong(key(property), value).apply()
    }

fun SharedPreferences.float(
    key: (KProperty<*>) -> String = KProperty<*>::name,
    defaultValue: Float = 0.toFloat()
): ReadWriteProperty<Any, Float> =
    object : ReadWriteProperty<Any, Float> {
        override fun getValue(thisRef: Any, property: KProperty<*>) =
            getFloat(key(property), defaultValue)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) =
            edit().putFloat(key(property), value).apply()
    }