package dev.stormery.photoassignment

import dev.stormery.photoassignment.di.testDataModule
import dev.stormery.photoassignment.di.testDomainModule
import org.junit.After
import org.junit.Before
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.test.KoinTest

abstract class BaseTest : KoinTest {
    @Before
    open fun setup() {
        stopKoin()
        startKoin {
            modules(
                testDataModule,
                testDomainModule,
            )
        }
    }

    @After
    open fun tearDown() {
        stopKoin()
    }
}