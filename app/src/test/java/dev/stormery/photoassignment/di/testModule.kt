package dev.stormery.photoassignment.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.stormery.photoassignment.database.JournalDatabase
import dev.stormery.photoassignment.domain.repository.JournalRepository
import dev.stormery.photoassignment.domain.usecase.AddJournalUseCase
import dev.stormery.photoassignment.domain.usecase.DeleteJournalUseCase
import dev.stormery.photoassignment.domain.usecase.GetJournalsUseCase
import org.koin.dsl.module
import org.mockito.Mockito

val testDataModule = module {
    single<SqlDriver> {
        // Use in-memory database for tests
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also {
            JournalDatabase.Schema.create(it)
        }
    }

    single<JournalDatabase> { JournalDatabase(get()) }

    single<JournalRepository> {
        Mockito.mock(JournalRepository::class.java)
    }
}

val testDomainModule = module {
    factory { AddJournalUseCase(get()) }
    factory { GetJournalsUseCase(get()) }
    factory { DeleteJournalUseCase(get()) }
}