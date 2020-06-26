package falih.example.bukuwarungtestapp.data.room.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Profile(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Long,
    val email: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val avatar: String? = null
)