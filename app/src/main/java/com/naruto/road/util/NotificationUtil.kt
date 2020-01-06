package com.naruto.road.util

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.naruto.core.base.app
import com.naruto.core.utils.Ext.notificationManager
import com.naruto.core.utils.LogUtil
import com.naruto.road.MainActivity
import com.naruto.road.R
import java.util.*

/**
 * 通知工具类
 */
object NotificationUtil {

    /**
     * 打开APP页面 配置path
     */
    val TYPE_APP_BY_PATH = 99

    val DEFAULT_CHANNEL_ID = "1024"
    val DEFAULT_CHANNEL_NAME = "Default"

    fun notification(title: String, body: String) {
        val random = Random().nextInt(999)
        val notificationIntent = Intent(app, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            app,
            random,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        //点击推送发送广播 在广播中处理跳转
        //测试以下机型广播方式跳转无效
        //华为P8max 5.1.1
//        val notificationIntent = Intent(app, BroadcastReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(app, random, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        notification(random, title, body, pendingIntent)
    }

    /**
     * 显示通知
     */
    fun notification(notifiId: Int, title: String, body: String, pendingIntent: PendingIntent) {
        try {
            createChannel(DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME)

            val builder = NotificationCompat.Builder(app, DEFAULT_CHANNEL_ID)
                .setContentTitle(title)//设置通知栏标题
                .setContentText(body)
                .setContentIntent(pendingIntent) //设置通知栏点击意图
                .setTicker(body) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setSmallIcon(R.mipmap.ic_launcher)//设置通知小ICON
                .setDefaults(Notification.DEFAULT_ALL)

            builder.setStyle(
                NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(
                    body
                )
            )//长文字折叠模式

            val notification = builder.build()
            notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
            notificationManager.notify(notifiId, notification)
        } catch (e: Exception) {
            LogUtil.e("NotificationUtil notification $e")
        } catch (oom: OutOfMemoryError) {
        }
    }

    /**
     * 创建通知渠道
     */
    fun createChannel(channelId: String, channelName: String) {
        //android8以上设置通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * 检测是否打开通知权限
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun checkNotificationsEnabled(): Boolean {
        return NotificationManagerCompat.from(app).areNotificationsEnabled()
    }

    /**
     * 去开启通知权限
     */
    fun enableNotifications(context: Context) {
        try {
            val intent = Intent()
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, app.packageName)
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                    intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                    intent.putExtra("app_package", app.packageName)
                    intent.putExtra("app_uid", app.applicationInfo.uid)
                }
                else -> {
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.putExtra("package", app.packageName)
                    intent.data = Uri.parse("package:${app.packageName}")
                }
            }
            context.startActivity(intent)
        } catch (e: Exception) {
        }
    }

    /**
     * 清除本应用发起的所有通知
     */
    fun cleanNotification() {
        try {
            notificationManager.cancelAll()
        } catch (ignore: Exception) {
        }
    }
}