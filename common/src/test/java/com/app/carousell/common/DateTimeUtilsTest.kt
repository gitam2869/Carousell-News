package com.app.carousell.common

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class DateTimeUtilsTest(val testName: String, val input: Long?, val expected: String) {

    @Test
    fun getTimeAgoFromSeconds() {
        val result = DateTimeUtils.getTimeAgoFromSeconds(input, 1724002445876L)
        Assert.assertEquals(expected, result)
    }

    /**
     * To test this dates i assume  Sunday, August 18, 2024 10:54:38 PM current time
     */
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): List<Array<Any?>> {
            return listOf(
                arrayOf("timeInNull_Empty", null, ""),
                arrayOf("timeInZero_Empty", 0L, ""),
                arrayOf("timeInSeconds_oneHourAgo", 1723995288L, "1 hour ago"),
                arrayOf("timeInSeconds_oneDayAgo", 1723908888L, "1 day ago"),
                arrayOf("timeInSeconds_twoDaysAgo", 1723822488L, "2 days ago"),
                arrayOf("timeInSeconds_fiveDayAgo", 1723563288L, "5 days ago"),
                arrayOf("timeInSeconds_sevenDayAgo", 1723396971L, "1 week ago"),
                arrayOf("timeInSeconds_oneWeekAgo", 1723390488L, "1 week ago"),
                arrayOf("timeInSeconds_twoWeeksAgo", 1722526488L, "2 weeks ago"),
                arrayOf("timeInSeconds_threeWeeksAgo", 1722180888L, "3 weeks ago"),
                arrayOf("timeInSeconds_oneMonthAgo", 1721489688L, "1 month ago"),
                arrayOf("timeInSeconds_seveMonthAgo", 1704114000L, "7 months ago"),
                arrayOf("timeInSeconds_twelveMonthAgo", 1692363600L, "1 year ago"),
            )
        }
    }

}