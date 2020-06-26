package falih.example.bukuwarungtestapp.model

import falih.example.bukuwarungtestapp.data.room.entity.Profile

data class UserListResponse(
    val `data`: List<Profile>,
    val page: Int,
    val per_page: Int,
    val total: Int,
    val total_pages: Int
)