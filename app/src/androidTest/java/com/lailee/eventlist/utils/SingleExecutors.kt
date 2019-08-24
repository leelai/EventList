package com.lailee.eventlist.utils

import com.lailee.eventlist.util.AppExecutors
import java.util.concurrent.Executor

class SingleExecutors : AppExecutors(instant, instant, instant) {
    companion object {
        private val instant = Executor { command -> command.run() }
    }
}