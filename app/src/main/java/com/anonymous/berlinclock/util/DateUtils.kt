package com.anonymous.berlinclock.util

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

fun DateTime.formattedData(
    pattern: String
): String = DateTimeFormat.forPattern(pattern).print(this)

fun String.getTimeMillis(
    pattern: String
) = DateTimeFormat.forPattern(pattern).parseDateTime(this).millis