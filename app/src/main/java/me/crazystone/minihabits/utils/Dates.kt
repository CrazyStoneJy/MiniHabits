package me.crazystone.minihabits.utils

import android.os.Build
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Calendar


data class DayWeek(
    val date: Long,
    val ym: String,      // yyyy年MM月
    val week: String,    // 一
    val day: String,     // 12
    val extra: String?,    // 今天
    val dateString: String
)

object Dates {

    fun getYM(): String {
        var year = 0
        var month = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentDate = LocalDate.now() // 获取当前日期
            year = currentDate.year // 获取当前年份
            month = currentDate.monthValue // 获取当前月份（1-12）
        } else {
            val calendar = Calendar.getInstance() // 获取当前时间的 Calendar 实例
            year = calendar.get(Calendar.YEAR) // 获取当前年份
            month = calendar.get(Calendar.MONTH) + 1 // 获取当前月份（注意：月份是从0开始的，所以需要加1）
        }
        return "$year 年 $month 月"
    }

    fun isBeforeToday(date: Long): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val today = LocalDate.now()
            val localDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
            return localDate.isBefore(today)
        } else {
            val today = Calendar.getInstance() // 获取当前时间的 Calendar 实例
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date
            return calendar.before(today)
        }
    }

    fun isToday(date: Long): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val today = LocalDate.now()
            val localDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
            return localDate.isEqual(today)
        } else {
            val today = Calendar.getInstance() // 获取当前时间的 Calendar 实例
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date
            return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                    calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)
        }
    }

    fun isAfterToday(date: Long): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val today = LocalDate.now()
            val localDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
            return localDate.isAfter(today)
        } else {
            val today = Calendar.getInstance() // 获取当前时间的 Calendar 实例
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date
            return calendar.after(today)
        }
    }

    fun getDate(date: Long): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val localDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
            val year = localDate.year
            val month = localDate.monthValue
            val day = localDate.dayOfMonth
            return "$year 年 $month 月 $day 日"
        } else {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date
            val actualYear = calendar.get(Calendar.YEAR)
            val actualMonth = calendar.get(Calendar.MONTH) + 1  // 记得加 1
            val actualDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            return "$actualYear 年 $actualMonth 月 $actualDayOfMonth 日"
        }
    }

    private fun transWeekNumber(weekNumber: Int): String {
        val weekLabels = mutableListOf("一", "二", "三", "四", "五", "六")
        if (weekNumber == 7) {
            return "日"
        } else {
            return weekLabels[weekNumber - 1]
        }
    }

    fun getDayOneWeek(): MutableList<DayWeek> {
        val dayWeeks = mutableListOf<DayWeek>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val today = LocalDate.now() // 获取当前日期
            (0..6).map {
                val current = today.plusDays(it.toLong())
                val year = current.year
                val month = current.monthValue
                val day = current.dayOfMonth
                val week = transWeekNumber(current.dayOfWeek.value)
                dayWeeks.add(
                    DayWeek(
                        date = current.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli() ,
                        ym = "$year 年 $month 月",
                        week = week,
                        day = "$day",
                        dateString = getDate(current.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()),
                        extra = if (it == 0) "今天" else null
                    )
                )
            }
        } else {
            val calendar = Calendar.getInstance() // 获取当前时间的 Calendar 实例
            (0..6).map {
                val current = calendar.clone() as Calendar
                current.add(Calendar.DAY_OF_YEAR, it)
                val actualYear = calendar.get(Calendar.YEAR)
                val actualMonth = calendar.get(Calendar.MONTH) + 1  // 记得加 1
                val actualDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                dayWeeks.add(
                    DayWeek(
                        date = current.timeInMillis,
                        ym = "$actualYear 年 $actualMonth 月",
                        week = transWeekNumber(dayOfWeek),
                        day = "$actualDayOfMonth",
                        extra = if (it == 0) "今天" else null,
                        dateString = getDate(current.timeInMillis)
                    )
                )
            }
        }
        return dayWeeks
    }

}