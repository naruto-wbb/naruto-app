package com.naruto.road.util

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.naruto.core.base.app

/**
 * 摇晃手机的监听
 */
class SensorManagerHelper : SensorEventListener {

    // 速度阈值，当摇晃速度达到这值后产生作用
     val SPEED_THRESHOLD  = 5000
    // 两次检测的时间间隔
     val UPDATE_INTERVAL_TIME = 100
    // 每次回调的时间间隔
    var LISTENER_INTERVAL_TIME = 2000L

    // 传感器管理器
    private var sensorManager: SensorManager? = null
    // 传感器
    private var sensor: Sensor? = null
    // 重力感应监听器
    private var onShakeListener: OnShakeListener? = null
    private var onShake: (() -> Unit)? = null
    // 手机上一个位置时重力感应坐标
    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private var lastZ: Float = 0f
    // 上次检测时间
    private var lastUpdateTime: Long = 0
    // 上次事件触发时间
    private var lastListenerCallBackTime: Long = 0

    init {
        // 获得传感器管理器
        sensorManager = app.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        // 获得重力传感器
        sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    // 停止检测
    fun stop() {
        sensorManager?.unregisterListener(this, sensor)
    }

    // 摇晃监听接口
    interface OnShakeListener {
        fun onShake()
    }

    // 设置重力感应监听器
    fun setOnShakeListener(listener: OnShakeListener) {
        if (sensor != null) sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
        onShakeListener = listener
    }

    fun setOnShakeListener(onShake: (() -> Unit)?) {
        if (sensor != null) sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
        this.onShake = onShake
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        var currentTimeMillis = System.currentTimeMillis()
        // 两次检测的时间间隔
        val checkTimeInterval = currentTimeMillis - lastUpdateTime
        val callBackTimeInterval = currentTimeMillis - lastListenerCallBackTime
        // 判断是否达到了检测时间间隔 以及 是否超过两次回调的间隔时间
        if (checkTimeInterval < UPDATE_INTERVAL_TIME) return
        // 现在的时间变成last时间
        lastUpdateTime = currentTimeMillis
        // 获得x,y,z坐标
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        // 获得x,y,z的变化值
        val deltaX = x - lastX
        val deltaY = y - lastY
        val deltaZ = z - lastZ
        // 将现在的坐标变成last坐标
        lastX = x
        lastY = y
        lastZ = z
        val speed = Math.sqrt((deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ).toDouble()) / checkTimeInterval * 10000
        // 达到速度阀值，发出提示
        if (speed >= SPEED_THRESHOLD && callBackTimeInterval > LISTENER_INTERVAL_TIME) {
            onShakeListener?.onShake()
            onShake?.let { it() }
            lastListenerCallBackTime = currentTimeMillis
        }
    }
}