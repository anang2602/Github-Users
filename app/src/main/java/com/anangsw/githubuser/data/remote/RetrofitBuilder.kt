package com.anangsw.githubuser.data.remote

import com.anangsw.githubuser.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitBuilder @Inject constructor(
    private val okhttpClientBuilder: OkhttpClientBuilder
) {

    fun <API> buildApi(
        api: Class<API>
    ): API {
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.client(okhttpClientBuilder.getOkhttpClient())
        return retrofitBuilder
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }

}