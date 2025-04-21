package dev.stormery.photoassignment.presentation

import dev.stormery.photoassignment.domain.usecase.DeleteJournalUseCase
import dev.stormery.photoassignment.domain.usecase.GetJournalsUseCase
import dev.stormery.photoassignment.presentation.model.JournalData
import dev.stormery.photoassignment.testUtils.TestData.createTestJournal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.kotlin.any
import org.mockito.kotlin.times

@ExperimentalCoroutinesApi
class MainScreenViewModelTest {

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    // Test dispatcher for coroutines
    private val testDispatcher = UnconfinedTestDispatcher()

    // Mocks
    private lateinit var getJournalsUseCase: GetJournalsUseCase
    private lateinit var deleteJournalUseCase: DeleteJournalUseCase
    private lateinit var viewModel: MainScreenViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getJournalsUseCase = mock(GetJournalsUseCase::class.java)
        deleteJournalUseCase = mock(DeleteJournalUseCase::class.java)
        viewModel = MainScreenViewModel(getJournalsUseCase, deleteJournalUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadJournals updates journals list`() = runTest {
        // Given
        val mockJournals = listOf(createTestJournal(), createTestJournal())
        `when`(getJournalsUseCase()).thenReturn(flowOf(mockJournals))

        // When
        viewModel.loadJournals()

        // Then
        assertEquals(mockJournals, viewModel.journals.value)
    }

    @Test
    fun `showNewEntryDialog should update state`() {
        // When
        viewModel.showNewEntryDialog()

        // Then
        assert(viewModel.showNewEntryDialog.value)
    }

    @Test
    fun `hideNewEntryDialog should update state`() {
        // Given
        viewModel.showNewEntryDialog()

        // When
        viewModel.hideNewEntryDialog()

        // Then
        assert(!viewModel.showNewEntryDialog.value)
    }
    @Test
    fun `deleteJournal should call use case`() = runTest {
        // Given
        val testJournal = createTestJournal()

        // Mock the GetJournalsUseCase to return an empty flow
        `when`(getJournalsUseCase()).thenReturn(flowOf(emptyList()))

        // When
        viewModel.deleteJournal(testJournal)

        // Then
        verify(deleteJournalUseCase).invoke(testJournal)
    }

}