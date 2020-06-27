package falih.example.bukuwarungtestapp.module

import android.content.Context
import android.content.SharedPreferences
import falih.example.bukuwarungtestapp.R
import falih.example.bukuwarungtestapp.data.repository.UserRepository
import falih.example.bukuwarungtestapp.data.repository.SessionRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositories = module {
    single { UserRepository(get(), get()) }
    single { SessionRepository(get()) }
    single { provideSharedPreferences(androidContext()) }
}

fun provideSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(context.getString(R.string.app_name) + "_preferences", Context.MODE_PRIVATE)
}