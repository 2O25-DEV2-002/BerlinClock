package com.anonymous.berlinclock.util

fun String.splitIntoIntParts(delimiter: String) = this.split(delimiter).map { it.toInt() }