package falih.example.bukuwarungtestapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import falih.example.bukuwarungtestapp.BuildConfig
import falih.example.bukuwarungtestapp.common.DATABASE_NAME
import falih.example.bukuwarungtestapp.common.DATABASE_VERSION
import falih.example.bukuwarungtestapp.data.room.dao.ProfileDao
import falih.example.bukuwarungtestapp.data.room.entity.Profile
import java.io.File

@Database(entities = [Profile::class], version = DATABASE_VERSION)
@TypeConverters(EntityFieldConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract  fun profileDao(): ProfileDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        internal fun getInstance(context: Context): AppDatabase? {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            var dbPath = ""
            if(BuildConfig.DEBUG){
                /*
                 * Debug app should use external file for easier debugging
                 */
                var file: File? = null
                try {
                    file = context.getExternalFilesDir(null)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                if (file != null) {
                    dbPath = file.absolutePath + "/databases/"
                }
            }

            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                dbPath + DATABASE_NAME
            )
                /*
                 * uncomment if you don't care about data from previous version
                 */
//                          .fallbackToDestructiveMigration()

                /*
                 * for easier testing so we don't have to use background thread,
                 * comment or remove it in production!
                 */
                //.allowMainThreadQueries()
                .build()
        }
    }
}