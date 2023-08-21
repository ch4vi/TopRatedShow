package com.ch4vi.topRatedShows.featureTVShows.domain.util

import androidx.lifecycle.Observer
import org.jetbrains.annotations.VisibleForTesting

class Event<out T>(private val content: T) {

    private var isUsed = false
    fun get(): T? {
        return if (isUsed) {
            null
        } else {
            isUsed = true
            content
        }
    }

    @VisibleForTesting
    fun forceGet(): T = content
}

class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {

    override fun onChanged(value: Event<T>) {
        value.get()?.let {
            onEventUnhandledContent(it)
        }
    }
}

fun <T> T.toEvent() = Event(this)
