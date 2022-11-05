package com.alecarnevale.claymore.generator.utils

internal fun StringBuilder.newLine(count: Int = 1) {
  repeat(count) {
    append("\n")
  }
}