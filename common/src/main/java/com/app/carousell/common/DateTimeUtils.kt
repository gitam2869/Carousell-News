package com.app.carousell.common

import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.Months
import org.joda.time.Years


object DateTimeUtils {

    fun getTimeAgoFromSeconds(timestampInSeconds: Long?, currentTimeInMillis: Long = DateTime.now().millis): String {
        if (timestampInSeconds == null || timestampInSeconds <= 0) return ""
        var timeInMillis = timestampInSeconds
        if(timestampInSeconds.toString().length == 10) //currently assume 10 digits represents seconds as per api response
            timeInMillis *= 1000

        return getTimeAgoFromMilliSeconds(timeInMillis, currentTimeInMillis)
    }

    fun getTimeAgoFromMilliSeconds(timeStampInMillis: Long, currentTimeInMillis: Long = DateTime.now().millis): String {
        val currentTimeStamp = DateTime(currentTimeInMillis)
        val timestamp = DateTime(timeStampInMillis)
        if (timestamp > currentTimeStamp) return ""

        val duration = Duration(timestamp, currentTimeStamp)
        val second = duration.standardSeconds
        val minutes = duration.standardMinutes
        val hours = duration.standardHours
        val days = duration.standardDays
        val weeks = days / 7
        val months = Months.monthsBetween(timestamp, currentTimeStamp).months
        val years = Years.yearsBetween(timestamp, currentTimeStamp).years

        if (years > 0) {
            val prefix = if (years == 1) "year" else "years"
            return "$years $prefix ago"
        }

        if (months > 0) {
            val prefix = if (months == 1) "month" else "months"
            return if (months == 12)
                "1 year ago"
            else "$months $prefix ago"
        }

        if (weeks > 0) {
            val prefix = if (weeks == 1L) "week" else "weeks"
            return if (weeks == 4L) // && (days % 7) > 1
                "1 month ago"
            else "$weeks $prefix ago"
        }

        if (days > 0) {
            val prefix = if (days == 1L) "day" else "days"
            return if (days == 7L)
                "1 week ago"
            else "$days $prefix ago"
        }

        if (hours > 0) {
            val prefix = if (hours == 1L) "hour" else "hours"
            return if (hours == 24L)
                "1 day ago"
            else "$hours $prefix ago"
        }

        if (minutes > 0) {
            val prefix = if (minutes == 1L) "minute" else "minutes"
            return if (minutes == 60L)
                "1 hour ago"
            else "$minutes $prefix ago"
        }

        return "$second seconds ago"
    }

}