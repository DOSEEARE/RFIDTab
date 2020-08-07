package com.example.rfidtab.di


import com.example.rfidtab.ui.auth.AuthViewModel
import com.example.rfidtab.ui.createkit.CreateKitViewModel
import com.example.rfidtab.ui.kitorder.KitOrderViewModel
import com.example.rfidtab.ui.profile.ProfileViewModel
import com.example.rfidtab.ui.search.SearchViewModel
import com.example.rfidtab.ui.task.TaskViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(androidApplication()) }
    viewModel { TaskViewModel(androidApplication()) }
    viewModel { ProfileViewModel(androidApplication()) }
    viewModel { CreateKitViewModel(androidApplication()) }
    viewModel { SearchViewModel(androidApplication()) }
    viewModel { KitOrderViewModel(androidApplication()) }
}