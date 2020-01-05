package com.naruto.road.util

import com.naruto.road.model.bean.User

object UserHelper {

    private var user: User? = null

    fun isLogin(): Boolean {
        return user?.id != -1L
    }
}