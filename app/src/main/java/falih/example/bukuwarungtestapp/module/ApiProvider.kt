package falih.example.bukuwarungtestapp.module

import falih.example.bukuwarungtestapp.api.APIServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import falih.example.bukuwarungtestapp.BuildConfig
import falih.example.bukuwarungtestapp.common.API_BASE_URL
import falih.example.bukuwarungtestapp.common.API_KEY
import falih.example.bukuwarungtestapp.common.API_TIMEOUT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val apiModules = module {
    factory { provideHttpLoggingInterceptor() }
    factory { provideGson() }
    factory { provideOkHttpClient(get()) }
    factory { provideApiServices(get()) }
    single { provideRetrofit(get(), get()) }
}

fun provideApiServices(retrofit: Retrofit): APIServices = retrofit.create(APIServices::class.java)

fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

fun provideGson(): Gson {
    return GsonBuilder()
//        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setLenient()
        .create()
}

fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

    val builder = OkHttpClient.Builder()

    builder
        .connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader(
                "X-Api-Key", API_KEY
            )
            chain.proceed(requestBuilder.build())
        }

    if (BuildConfig.DEBUG) {
        builder.addInterceptor(httpLoggingInterceptor)
    }

    return builder.build()
}

fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return interceptor
}