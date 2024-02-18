package com.lvsmsmch.lchat

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import com.lvsmsmch.lchat.di.appModule
import com.lvsmsmch.lchat.utils.RemoteConfigHelper
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (isDifferentProcess()) return

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }

        RemoteConfigHelper.setDefaultsAndFetch()
    }

    private fun isDifferentProcess(): Boolean {
        return packageName != (getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)
            ?.runningAppProcesses?.filterNotNull()?.firstOrNull { it.pid == Process.myPid() }
            ?.processName
    }
}