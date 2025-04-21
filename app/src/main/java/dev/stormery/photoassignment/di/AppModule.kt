package dev.stormery.photoassignment.di

import app.cash.sqldelight.db.SqlDriver
import dev.stormery.photoassignment.data.local.DatabaseDriverFactory
import dev.stormery.photoassignment.data.repository.JournalRepositoryImpl
import dev.stormery.photoassignment.database.JournalDatabase
import dev.stormery.photoassignment.domain.repository.JournalRepository
import dev.stormery.photoassignment.domain.usecase.AddJournalUseCase
import dev.stormery.photoassignment.domain.usecase.DeleteJournalUseCase
import dev.stormery.photoassignment.domain.usecase.GetJournalsUseCase
import dev.stormery.photoassignment.presentation.MainScreenViewModel
import dev.stormery.photoassignment.presentation.NewJournalViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { MainScreenViewModel(get(),get()) }
    viewModel { NewJournalViewModel(get()) }
}

val dataModule = module {
    single<SqlDriver>{
        DatabaseDriverFactory(androidContext()).createDriver()
    }
    single<JournalDatabase> {
        JournalDatabase(get())
    }
}

val domainModule = module{
    single<JournalRepository> {
        JournalRepositoryImpl(get())
    }

    single {
        AddJournalUseCase(get())
    }
    single {
        GetJournalsUseCase(get())
    }
    single {
        DeleteJournalUseCase(get())
    }
}