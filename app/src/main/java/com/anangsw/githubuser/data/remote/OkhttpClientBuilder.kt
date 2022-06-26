package com.anangsw.githubuser.data.remote

import com.anangsw.githubuser.BuildConfig
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class OkhttpClientBuilder {

    fun getOkhttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val buider = OkHttpClient.Builder()

        if (isCerfValid()) {
            val pinUrl = BuildConfig.BASE_URL.replaceFirst("https://", "")
            val cerfPinner = CertificatePinner.Builder()
                .add(pinUrl, BuildConfig.CA_GITHUB_1)
                .add(pinUrl, BuildConfig.CA_GITHUB_2)
                .build()
            buider.certificatePinner(cerfPinner)
        }

        val httpClient = buider
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(logging)
        }
        return buider.build()
    }

    private fun isCerfValid(): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val expired = dateFormat.parse(BuildConfig.CA_EXP_DATE)
        val df = DateFormat.getTimeInstance()
        df.timeZone = TimeZone.getTimeZone("UTC")
        val currentDate = df.calendar.time
        return currentDate.before(expired)
    }

}