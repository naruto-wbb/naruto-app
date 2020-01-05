package com.naruto.road.model

import com.naruto.core.base.bean.BaseResponse
import com.naruto.core.net.BaseRepository
import com.naruto.road.model.bean.BannerResponse

object HomeRepository : BaseRepository() {

    suspend fun getBanner(): BaseResponse<List<BannerResponse>> {
        return executeResponse({ ApiService.api.getBanner() })
    }

}