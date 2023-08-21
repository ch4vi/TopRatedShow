package com.ch4vi.topRatedShows.utils

import java.io.InputStreamReader

internal fun Any.getJsonContent(fileName: String): String {
    return InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(fileName)).use { it.readText() }
}
