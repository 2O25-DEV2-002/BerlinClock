package com.anonymous.berlinclock.util

fun String.splitIntoIntParts(
    delimiter: String
) = this.split(delimiter).map { it.toInt() }

fun String.getLampTag(
    name: String,
    color: String
) = "$this-$name-$color"