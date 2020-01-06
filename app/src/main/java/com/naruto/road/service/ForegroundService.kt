package com.naruto.road.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.naruto.core.base.app
import com.naruto.road.R
import com.naruto.road.util.NotificationUtil

/**
 * 启动前台服务 展示常驻通知
 */
class ForegroundService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * START_STICKY     粘性的  service强制杀死后，会尝试重新启动service，不会传递原来的intent（null）
     * START_NOT_STICKY 非粘性的 service强制杀死后，不会尝试重新启动service
     * START_REDELIVER_INTENT     service强制杀死后，会尝试重新启动service，会传递原来的intent
     * START_STICKY_COMPATIBILITY  START_STICKY的兼容版本，但不保证服务被kill后一定能重启
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()
        return START_REDELIVER_INTENT
    }

    private fun showNotification() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationUtil.createChannel(
                    NotificationUtil.DEFAULT_CHANNEL_ID, NotificationUtil.DEFAULT_CHANNEL_NAME
                )
                startForeground(
                    1024,
                    NotificationCompat.Builder(app, NotificationUtil.DEFAULT_CHANNEL_ID).build()
                )
            } else {
                NotificationUtil.notification(
                    app.getString(R.string.app_name),
                    app.getString(R.string.app_name) + "正在运行"
                )
            }
        } catch (ignore: Exception) {
        }
    }

    companion object {

        fun startService(context: Context) {
            val intent = Intent(context, ForegroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }
}