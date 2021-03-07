package com.daly.weatherinfos.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

const val API_KEY = "bea1f3e5644b186568fadd80d50d315c"

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request()
        // DONT INCLUDE API KEYS IN YOUR SOURCE CODE
        val url = req.url().newBuilder().addQueryParameter("APPID", API_KEY).build()
        req = req.newBuilder().url(url).build()
        return chain.proceed(req)
    }
}