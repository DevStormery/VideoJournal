package dev.stormery.photoassignment.domain

import dev.stormery.photoassignment.domain.repository.JournalRepository
import dev.stormery.photoassignment.domain.usecase.AddJournalUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class AddJournalUseCaseTest {
    private lateinit var repository: JournalRepository
    private lateinit var useCase: AddJournalUseCase

    @Before
    fun setUp() {
        repository = mock()
        useCase = AddJournalUseCase(repository)
    }

    @Test
    fun `invoke calls insertJournal with correct parameters`() = runTest {
        val filePath = "file/path"
        val videoPath = "video/path"
        val description = "Test description"
        val timestamp = 123456789L

        useCase.invoke(filePath, videoPath, description, timestamp)

        verify(repository).insertJournal(filePath, videoPath, description, timestamp)
    }
}