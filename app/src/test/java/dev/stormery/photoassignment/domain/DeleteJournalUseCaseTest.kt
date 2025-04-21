package dev.stormery.photoassignment.domain

import dev.stormery.photoassignment.domain.repository.JournalRepository
import dev.stormery.photoassignment.domain.usecase.DeleteJournalUseCase
import dev.stormery.photoassignment.testUtils.TestData.createTestJournal
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.times

class DeleteJournalUseCaseTest {

    private val repository: JournalRepository = mock()
    private val useCase = DeleteJournalUseCase(repository)

    @Test
    fun `invoke calls deleteJournal on repository`() = runTest {
        val journal = createTestJournal()

        useCase.invoke(journal)

        verify(repository, times(1)).deleteJournal(journal)
    }
}
