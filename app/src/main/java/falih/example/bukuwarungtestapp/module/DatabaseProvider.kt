package falih.example.bukuwarungtestapp.module

import android.content.Context
import falih.example.bukuwarungtestapp.data.room.AppDatabase
import falih.example.bukuwarungtestapp.data.room.dao.ProfileDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModules = module {
    single { provideDatabase(androidContext()) }
    factory { provideProfileDao(get()) }
}

fun provideDatabase(context: Context): AppDatabase {
    return AppDatabase.getInstance(context)!!
}

fun provideProfileDao(appDatabase: AppDatabase): ProfileDao {
    return appDatabase.profileDao()
}
