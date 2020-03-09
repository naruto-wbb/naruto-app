package com.naruto.road.model

import com.naruto.core.base.app
import com.naruto.core.net.BaseHttpClient
import com.naruto.road.Config
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File

object ApiService : BaseHttpClient() {

    val api by lazy { createService(Config.BASE_URL, Api::class.java) }

    override fun handleOkHttp(builder: OkHttpClient.Builder) {
        val httpCacheDirectory = File(app.cacheDir, "naruto")
        val cacheSize = 10 * 1024 * 1024L // 10 MiB
        val cache = Cache(httpCacheDirectory, cacheSize)
        builder.cache(cache)
//            .cookieJar(PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(App.CONTEXT)))
//            .addInterceptor { chain ->
//                var request = chain.request()
//                if (!NetWorkUtils.isNetworkAvailable(app)) {
//                    request = request.newBuilder()
//                        .cacheControl(CacheControl.FORCE_CACHE)
//                        .build()
//                }
//                val response = chain.proceed(request)
//                if (!NetWorkUtils.isNetworkAvailable(app)) {
//                    val maxAge = 60 * 60
//                    response.newBuilder()
//                        .removeHeader("Pragma")
//                        .header("Cache-Control", "public, max-age=$maxAge")
//                        .build()
//                } else {
//                    val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
//                    response.newBuilder()
//                        .removeHeader("Pragma")
//                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
//                        .build()
//                }
//                response
//            }
    }
}