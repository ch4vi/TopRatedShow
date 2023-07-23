package com.ch4vi.distilled.utils

import java.io.InputStreamReader

internal fun Any.getJsonContent(fileName: String): String {
    return InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(fileName)).use { it.readText() }
}
