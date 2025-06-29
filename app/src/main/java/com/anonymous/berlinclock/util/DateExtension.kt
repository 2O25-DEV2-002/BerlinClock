package com.anonymous.berlinclock.util

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

fun DateTime.formattedData(
    pattern: String
): String = DateTimeFormat.forPattern(pattern).print(this)