package com.lailee.eventlist

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
    }
}