package falih.example.bukuwarungtestapp

import androidx.multidex.MultiDexApplication
import falih.example.bukuwarungtestapp.module.apiModules
import falih.example.bukuwarungtestapp.module.repositories
import falih.example.bukuwarungtestapp.module.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : MultiDexApplication() {

    companion object {
        /*
         * Singleton pattern for Application.
         * to refer to this object, use:
         * MyApp.getInstance()
         *
         * in alternative, you could also use:
         * (application as MyApp) or (getApplication() as MyApp)
         */
        private lateinit var mInstance: MyApp

        // Getter for singleton MyApp
        @Synchronized
        fun getInstance(): MyApp {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(listOf(
                apiModules,
                repositories,
                viewModels
            ))
        }
    }
}