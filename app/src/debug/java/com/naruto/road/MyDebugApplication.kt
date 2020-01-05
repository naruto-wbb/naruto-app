package com.naruto.road

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.naruto.core.base.BaseDebugApplication

class MyDebugApplication : BaseDebugApplication() {

    val br = ScreenReceiver()

    override fun onCreate() {
        super.onCreate()


        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
                unregisterReceiver(br)
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                val intentFilter = IntentFilter()
                intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
                intentFilter.addAction(Intent.ACTION_SCREEN_ON)
                registerReceiver(br, intentFilter)
            }
        })
    }

    class ScreenReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (Intent.ACTION_SCREEN_OFF == intent.action){

            }else if (Intent.ACTION_SCREEN_ON == intent.action){
                context.sendBroadcast(Intent("finish activity"))
            }
        }
    }
}