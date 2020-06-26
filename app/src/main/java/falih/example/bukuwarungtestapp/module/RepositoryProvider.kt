package falih.example.bukuwarungtestapp.module

import falih.example.bukuwarungtestapp.data.repository.ProfileRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositories = module {
    single { ProfileRepository(androidContext(), get()) }
}