package com.naruto.road

import com.naruto.core.base.app

object Config {

    //api url
    val baseUrl = app.resources.getString(R.string.base_url)

    //SharedPreferences相关
    const val DEBUG_WEB_VIEW_CACHE = "DEBUG_WEB_VIEW_CACHE"

    //intent相关参数
    const val TITLE = "TITLE"
    const val URL = "URL"
}