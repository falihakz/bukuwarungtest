package falih.example.bukuwarungtestapp.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import falih.example.bukuwarungtestapp.data.room.entity.Profile

@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg note: Profile)

    @Query("SELECT * FROM profile")
    fun getAll(): LiveData<List<Profile>>

    @Query("DELETE FROM profile WHERE id = :id")
    fun delete(id: Long)
}
