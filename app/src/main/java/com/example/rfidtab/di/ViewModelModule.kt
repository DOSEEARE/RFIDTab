package com.example.rfidtab.di


import com.example.rfidtab.ui.auth.AuthViewModel
import com.example.rfidtab.ui.task.TasksViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(androidApplication()) }
    viewModel { TasksViewModel(androidApplication()) }
}