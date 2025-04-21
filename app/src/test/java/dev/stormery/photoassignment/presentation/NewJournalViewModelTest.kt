package dev.stormery.photoassignment.presentation

import android.net.Uri
import app.cash.turbine.test
import dev.stormery.photoassignment.domain.usecase.AddJournalUseCase
import dev.stormery.photoassignment.presentation.camera.UIEvent
import dev.stormery.photoassignment.testUtils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class NewJournalViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val addJournalUseCase = mock(AddJournalUseCase::class.java)
    private val viewModel = NewJournalViewModel(addJournalUseCase)

    @Test
    fun `onCameraPermissionGranted emits StartVideoCapture event`() = runTest {
        viewModel.uiEvent.test {
            viewModel.onCameraPermissionGranted()
            assert(awaitItem() == UIEvent.StartVideoCapture)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onPermissionDenied emits ShowPermissionDenied event`() = runTest {
        viewModel.uiEvent.test {
            viewModel.onPermissionDenied()
            assert(awaitItem() == UIEvent.ShowPermissionDenied)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onPermanentlyDenied emits OnPermanentlyDenied event`() = runTest {
        viewModel.uiEvent.test {
            viewModel.onPermanentlyDenied()
            assert(awaitItem() == UIEvent.OnPermanentlyDenied)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setVideoUri updates journalData with correct URI`() = runTest {
        val testUri = Uri.parse("content://media/external/video/media/1")
        viewModel.setVideoUri(testUri)

        advanceUntilIdle()

        assertEquals(testUri, viewModel.journalData.value.videoUri)
    }

    @Test
    fun `setDescription updates journalData with correct description`() = runTest {
        val description = "Test Description"
        viewModel.setDescription(description)
        advanceUntilIdle()
        assertEquals(description, viewModel.journalData.value.description)
    }

    @Test
    fun `setTimestamp updates journalData with correct timestamp`() = runTest {
        val timestamp = 123456789L
        viewModel.setTimestamp(timestamp)
        advanceUntilIdle()
        assertEquals(timestamp, viewModel.journalData.value.timestamp)
    }

    @Test
    fun `setVideoPath updates journalData with correct video path`() = runTest {
        val videoPath = "/path/to/video"
        viewModel.setVideoPath(videoPath)
        advanceUntilIdle()
        assertEquals(videoPath, viewModel.journalData.value.videoPath)
    }
    @Test
    fun `saveJournal invokes addJournalUseCase and calls onSavedEntry`() = runTest {
        val testUri = Uri.parse("test://video")
        val videoPath = "/path/to/video"
        val description = "Test Description"
        val timestamp = 123456789L

        viewModel.setVideoUri(testUri)
        viewModel.setVideoPath(videoPath)
        viewModel.setDescription(description)
        viewModel.setTimestamp(timestamp)

        val onSavedEntry = mock(Runnable::class.java)

        viewModel.saveJournal { onSavedEntry.run() }
        advanceUntilIdle()
        verify(addJournalUseCase).invoke(
            testUri.toString(),
            videoPath,
            description,
            timestamp
        )
        verify(onSavedEntry).run()
    }
    @Test
    fun `resetEntryState resets journalData to default values`() = runTest {
        viewModel.setVideoUri(Uri.parse("test://video"))
        viewModel.setVideoPath("/path/to/video")
        viewModel.setDescription("Test Description")
        viewModel.setTimestamp(123456789L)

        viewModel.resetEntryState()
        advanceUntilIdle()
        val journal = viewModel.journalData.value
        assertNull(journal.videoUri)
        assertEquals("", journal.videoPath)
        assertNull(journal.description)
        assertTrue(journal.timestamp > 0)
    }
}
