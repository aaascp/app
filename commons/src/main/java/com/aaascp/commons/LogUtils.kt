package com.aaascp.commons

import android.util.Log
import timber.log.Timber

fun initLoggerForDebug() {
    Timber.plant(Timber.DebugTree())
}

fun initLogger() {
    Timber.plant(CrashReportingTree(LogLibrary()))
}

private class CrashReportingTree(private val logger: LoggerContract) : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }
        logger.log(priority, tag, message)
        if (t != null) {
            if (priority == Log.ERROR) {
                logger.logError(t)
            } else if (priority == Log.WARN) {
                logger.logWarning(t)
            }
        }
    }
}

private interface LoggerContract {
    fun log(priority: Int, tag: String?, message: String)

    fun logWarning(t: Throwable)

    fun logError(t: Throwable)
}

private const val LOGGER_TAG = "Logger"

private class LogLibrary : LoggerContract {
    override fun log(priority: Int, tag: String?, message: String) {
        Log.d(tag, message)
    }

    override fun logWarning(t: Throwable) {
        Log.d(LOGGER_TAG, t.message ?: t.cause.toString())
    }

    override fun logError(t: Throwable) {
        Log.d(LOGGER_TAG, t.message ?: t.cause.toString())
    }
}
