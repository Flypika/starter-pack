package com.flypika.pack.presentation.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    const val DDoMMoYYYY = "dd.MM.yyyy"
    const val DDoMMMM = "dd MMMM"
    const val DD_MM_YYYY = "dd MM yyyy"
    const val D_MMM_YYYYo_E = "d MMM yyyy, E"
    const val DDoMMoYYYY_HH_MM_SS = "dd.MM.yyyy HH:mm:ss"
    const val ISO8601 = "yyyy-MM-dd'T'HH:mm:ssZ"
    const val YYYY_MM_DD = "yyyy-MM-dd"
    const val ISO8601_SSS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val ISO8601_DT = "yyyy-MM-dd'T'HH:mm:ss"
    const val DDoMMoYYYY_HH_MM = "yyyy-MM-dd HH:mm"
    const val yyyy_MM_dd_HH_MM = "yyyy-MM-dd'T'HH:mm"
    const val YYYY = "yyyy"
    const val MONTH_PICKER_FORMAT = "MM.yyyy"
    const val YYYY_MM = "yyyy-MM"
    const val HH_MM = "HH:mm"
    const val yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss"
    const val ddLLLLyyyy = "dd LLLL yyyy"
    const val dMMyyyy_HH_mm = "d.MM.yyyy,HH:mm"
    const val dd_LLLL = "dd LLLL"
    const val dd_LLLL_HH_mm = "dd LLLL HH:mm"
    const val ddIMM = "dd/MM"
}

fun String.toDate(pattern: String = DateUtils.ISO8601_SSS, timeZone: TimeZone = TimeZone.getTimeZone("GMT")): Date? {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    dateFormat.timeZone = timeZone
    return try {
        dateFormat.parse(this)
    } catch (e: ParseException) {
        null
    }
}

fun Date.toStringDate(pattern: String = DateUtils.HH_MM, timeZone: TimeZone = TimeZone.getTimeZone("GMT")): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    dateFormat.timeZone = timeZone
    return dateFormat.format(this)
}

fun Calendar.isSameDay(calendar2: Calendar?): Boolean {
    return calendar2 != null &&
            get(Calendar.DATE) == calendar2.get(Calendar.DATE)
}

fun Calendar.isToday(): Boolean {
    return this.isEqualsByDate(Calendar.getInstance())
}

fun Calendar.isEqualsByDate(calendar2: Calendar?): Boolean {
    return calendar2 != null &&
            get(Calendar.DATE) == calendar2.get(Calendar.DATE) &&
            get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
            get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
}

fun Calendar.minusDays(days: Int): Calendar {
    add(Calendar.DAY_OF_YEAR, -days)
    return this
}

fun Calendar.plusDays(days: Int): Calendar {
    add(Calendar.DAY_OF_YEAR, days)
    return this
}

fun Calendar.plusHours(days: Int): Calendar {
    add(Calendar.HOUR_OF_DAY, days)
    return this
}

fun Calendar.minusMinute(minute: Int): Calendar {
    add(Calendar.MINUTE, -minute)
    return this
}

fun Calendar.plusMinute(minute: Int): Calendar {
    add(Calendar.MINUTE, minute)
    return this
}

fun Date.toCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar
}

fun Calendar.setZeroTime(): Calendar {
    set(Calendar.MILLISECOND, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.HOUR_OF_DAY, 0)
    return this
}

fun Calendar.setEndTime(): Calendar {
    set(Calendar.MILLISECOND, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MINUTE, 59)
    set(Calendar.HOUR_OF_DAY, 23)
    return this
}