package com.daly.weatherinfos.depinjection

import com.daly.weatherinfos.BuildConfig
import com.daly.weatherinfos.network.apis.WeatherApi
import com.daly.weatherinfos.network.interceptors.AuthInterceptor
import com.daly.weatherinfos.network.utils.ResponseHandler
import com.daly.weatherinfos.repositories.WeatherRepository
import com.daly.weatherinfos.ui.WeatherFragment
import com.daly.weatherinfos.utils.FragmentPreferencesUtils
import com.daly.weatherinfos.viewmodels.WeatherViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val weatherFragmentModule = module {
    factory { WeatherFragment() }
}

val prefModule = module {
    single { FragmentPreferencesUtils(androidContext()) }
}

val viewModelModule = module {
    viewModel {
        WeatherViewModel(get())
    }
}

val networkModule = module {
    factory { AuthInterceptor() }
    factory { provideOkHttpClient(get(), get()) }
    factory { provideHttpLoggingInterceptor() }
    factory { provideForecastApi(get()) }
    single { provideRetrofit(get()) }
    factory { ResponseHandler() }
}

val forecastModule = module {
    factory { WeatherRepository(get(), get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideOkHttpClient(authInterceptor: AuthInterceptor, httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient().newBuilder()
    .addInterceptor(authInterceptor)
    .addInterceptor(httpLoggingInterceptor)
    .build()

fun provideHttpLoggingInterceptor() : HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
}

fun provideForecastApi(retrofit: Retrofit): WeatherApi = retrofit.create(WeatherApi::class.java)