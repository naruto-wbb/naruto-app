package com.naruto.road.activity

import android.os.Build
import androidx.annotation.NonNull
import android.text.TextUtils
import android.view.View
import android.webkit.*
import android.webkit.WebView
import androidx.core.view.isNotEmpty
import com.naruto.core.base.BaseActivity
import com.naruto.core.utils.JsonUtils
import com.naruto.core.utils.SPUtils
import com.naruto.core.utils.ToastUtil
import com.naruto.road.BuildConfig
import com.naruto.road.Config
import com.naruto.road.R
import com.naruto.road.util.SensorManagerHelper
import com.naruto.road.util.UserHelper
import kotlinx.android.synthetic.main.activity_events.*
import kotlinx.android.synthetic.main.view_title_bar.*
import org.jetbrains.annotations.Nullable

/**
 * H5页面
 */
class EventsActivity : BaseActivity() {

    private var title: String? = ""
    private var url: String? = ""

    private var sensorHelper: SensorManagerHelper? = null

    override fun getLayout() = R.layout.activity_events

    override fun initView() {
        initWebView()
        initWebViewClient()
        loadUrl()
        tv_title.text = title
        iv_back.setOnClickListener { onBackPressed() }

        //初始化加速度传感器管理类
        sensorHelper = SensorManagerHelper()
    }

    private fun initWebViewClient() {
        mWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val requestUtl = request.url.toString()
                    view.loadUrl(requestUtl)
                }
                return false
            }
        }

        mWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressBar.progress = newProgress
                if (newProgress == 100) {
                    progressBar.visibility = View.GONE
                }
                super.onProgressChanged(view, newProgress)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                title?.let {

                }
            }

            //Js.log()回调方法 包含Js系统报错
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                if (BuildConfig.DEBUG) {
                    ToastUtil.showLong(consoleMessage?.message())
                }

                return super.onConsoleMessage(consoleMessage)
            }
        }
    }

    private fun loadUrl() {
        if (!TextUtils.isEmpty(url))
            mWebView.loadUrl(url)
    }

    override fun initData() {
        title = intent.getStringExtra(Config.TITLE) ?: ""
        url = intent.getStringExtra(Config.URL) ?: ""

        //解析data参数
        try {
            val data = intent?.data
            if (url.isNullOrBlank() && null != data) {
                data.getQueryParameter(Config.TITLE)?.let { title = it }
                data.getQueryParameter(Config.URL)?.let { url = it }
            }
        } catch (ignore: Exception) {
        }
    }

    private fun initWebView() {
        val settings = mWebView.settings
        //SDK_API > 16 自动播放音视频
        settings.mediaPlaybackRequiresUserGesture = false
        settings.javaScriptEnabled = true
        /* 打开缓存 */
        settings.setAppCacheEnabled(true)
        // 设置缓存模式,开发包不读缓存 正式包使用默认的缓存模式
        settings.cacheMode = if (BuildConfig.DEBUG) {
            if (SPUtils.getInstance().getBoolean(Config.DEBUG_WEB_VIEW_CACHE, false)) {
                WebSettings.LOAD_DEFAULT
            } else {
                WebSettings.LOAD_NO_CACHE
            }
        } else WebSettings.LOAD_DEFAULT
        /* 设置为true表示支持使用js打开新的窗口 */
        settings.javaScriptCanOpenWindowsAutomatically = true
        /* 大部分网页需要自己保存一些数据,这个时候就的设置下面这个属性 */
        settings.domStorageEnabled = true
        /* 设置为使用webView推荐的窗口 */
        settings.useWideViewPort = true
        /* 设置网页自适应屏幕大小 ---这个属性应该是跟上面一个属性一起用 */
        settings.loadWithOverviewMode = true
        /* 设置是否允许webView使用缩放的功能,false,不允许 */
        settings.builtInZoomControls = false
        /* 提高网页渲染的优先级 */
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        /* 设置显示水平滚动条,就是网页右边的滚动条.不显示 */
        mWebView.isHorizontalScrollBarEnabled = false
        /* 指定垂直滚动条是否有叠加样式 */
        mWebView.setVerticalScrollbarOverlay(true)
        /* 设置滚动条的样式 */
        mWebView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        mWebView.addJavascriptInterface(this, "control")
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterOnShakeListener()
        if (mWebView != null) {
            mWebView.destroy()
        }
    }

    /**
     * 获取APP版本
     */
    @JavascriptInterface
    fun getVersion() {
        runOnUiThread {
            try {
                val versionCode = packageManager.getPackageInfo(packageName, 0).versionCode
                mWebView.evaluateJavascript("javascript:versionData($versionCode)", null)
            } catch (e: Exception) {
            }
        }
    }

    /**
     * api通用接口
     * @param checkLogin 是否登录校验
     * @param needRefresh 登录成功是否刷新页面
     * @param path 请求api拼接path
     * @param params 请求api参数
     * @param callback 回调js方法名
     */
    @JavascriptInterface
    fun getDataForJson(
        checkLogin: Boolean,
        needRefresh: Boolean, @NonNull path: String, @Nullable params: String, @Nullable callback: String
    ) {
        if (path.isNullOrEmpty()) {
            if (BuildConfig.DEBUG) ToastUtil.showLong("path can not be null")
            return
        }

        var paramMap: Map<String, String>? = null
        if (!params.isNullOrEmpty()) {
            try {
                paramMap = JsonUtils.json2Maps(params)
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) ToastUtil.showLong("check params format")
            }
        }

        runOnUiThread {
            if (checkLogin && !UserHelper.isLogin()) {

            } else {

            }
        }
    }

    /**
     * 活动api通用接口请求成功
     */
    fun getActivityJsonDataSuccess(data: String, callback: String) {
        runOnUiThread {
            if (!callback.isNullOrEmpty()) {
                mWebView.evaluateJavascript("javascript:$callback($data)", null)
            }
        }
    }

    /**
     * 刷新当前页面
     */
    private fun reload() {
        mWebView?.reload()
    }

    @JavascriptInterface
    fun toast(data: String) {
        runOnUiThread {
            ToastUtil.showLong(data)
        }
    }

    override fun onBackPressed() {
        mWebView.evaluateJavascript("javascript:onBackPressed()", null)
        unregisterOnShakeListener()
        if (mWebView != null) {
            if (mWebView.canGoBack()) {
                mWebView.goBack()
            } else {
                super.onBackPressed()
                if (mWebView.isNotEmpty()) {
                    mWebView.destroy()
                }
            }
        } else {
            super.onBackPressed()
        }
    }

    /**
     * 注册加速度传感器
     */
    @JavascriptInterface
    fun registerOnShakeListener(intervalTime: Long = 2000) {
        sensorHelper?.LISTENER_INTERVAL_TIME = intervalTime
        sensorHelper?.setOnShakeListener { onShake() }
    }

    /**
     * 解绑加速度传感器
     */
    @JavascriptInterface
    fun unregisterOnShakeListener() {
        sensorHelper?.stop()
    }

    /**
     * 确认当前动作为摇一摇 通知H5
     */
    private fun onShake() {
        mWebView.evaluateJavascript("javascript:onShake()", null)
        ToastUtil.showLong("shake")
    }
}