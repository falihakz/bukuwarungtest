package falih.example.bukuwarungtestapp.module

import falih.example.bukuwarungtestapp.ui.home.HomeViewModel
import falih.example.bukuwarungtestapp.ui.home.userdetail.UserDetailViewModel
import falih.example.bukuwarungtestapp.ui.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModels = module {
    viewModel { HomeViewModel(get()) }
    viewModel { ProfileViewModel() }
    viewModel { UserDetailViewModel() }
}