package com.naruto.road.model

import com.naruto.core.base.bean.BaseResponse
import com.naruto.road.model.bean.BannerResponse
import retrofit2.http.GET

interface Api {

    @GET("/banner/json")
    suspend fun getBanner(
//        @Field("page") page: String
    ): BaseResponse<List<BannerResponse>>


}