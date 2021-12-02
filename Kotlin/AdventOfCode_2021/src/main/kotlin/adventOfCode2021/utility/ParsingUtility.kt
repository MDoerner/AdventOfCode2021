package adventOfCode2021.utility

fun String.tryToInt(): Int? = (try { this.toInt() } catch (e: NumberFormatException) { null })