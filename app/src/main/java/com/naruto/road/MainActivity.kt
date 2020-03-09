package com.naruto.road

import android.content.Context
import com.naruto.core.base.BaseActivity
import com.naruto.core.utils.LogUtil
import com.naruto.road.model.HomeRepository
import com.naruto.road.view.FixLinearLayoutManager
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private var mAdapter: MainAdapter? = null

    private var fromCount = 0


    override fun getLayout(): Int = R.layout.activity_main

    override fun initView() {
        rv.layoutManager = FixLinearLayoutManager(this)
        mAdapter = MainAdapter(null)
        mAdapter?.bindToRecyclerView(rv)

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context: Context, layout: RefreshLayout? ->
            MaterialHeader(context).setColorSchemeColors(context.resources.getColor(R.color.colorAccent))
        }
        refreshLayout.setOnRefreshListener {
            fromCount = 0
            getData()
        }
        refreshLayout.setOnLoadMoreListener {
            fromCount++
            getData()
        }
    }

    override fun initData() {
        launch {
            LogUtil.e(Thread.currentThread().name)
            var response = HomeRepository.getBanner()
            LogUtil.e(response.data.toString())
        }
    }

    fun getData() {

    }
}