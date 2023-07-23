package com.ch4vi.distilled

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class TopRatedShowTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        appName: String?,
        context: Context?
    ): Application = super.newApplication(cl, HiltTestApplication::class.java.name, context)
}
