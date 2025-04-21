package dev.stormery.photoassignment.domain

import android.net.Uri
import dev.stormery.photoassignment.domain.repository.JournalRepository
import dev.stormery.photoassignment.domain.usecase.GetJournalsUseCase
import dev.stormery.photoassignment.presentation.model.JournalData
import dev.stormery.photoassignment.testUtils.TestData.sampleJournals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class GetJournalsUseCaseTest {

    private val repository: JournalRepository = mock()
    private val useCase = GetJournalsUseCase(repository)

    @Test
    fun `invoke returns expected list of JournalData`() = runTest {
        val expectedJournals = sampleJournals

        whenever(repository.getJournals()).thenReturn(flowOf(expectedJournals))

        val result = useCase.invoke().toList()

        assertEquals(expectedJournals, result.first())
    }
}
