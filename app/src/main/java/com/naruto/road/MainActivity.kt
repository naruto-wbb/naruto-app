package com.naruto.road

import com.naruto.core.base.BaseActivity
import com.naruto.core.utils.LogUtil
import com.naruto.road.model.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    override fun getLayout(): Int = R.layout.activity_main

    override fun initView() {


    }

    override fun initData() {
        launch {
            LogUtil.e(Thread.currentThread().name)
            var response = HomeRepository.getBanner()
            LogUtil.e(response.data.toString())

        }

    }
}