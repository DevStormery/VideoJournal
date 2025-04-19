package dev.stormery.photoassignment.di

import dev.stormery.photoassignment.presentation.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { MainScreenViewModel() }
}