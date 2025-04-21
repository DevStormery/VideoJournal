package dev.stormery.photoassignment.testUtils

import dev.stormery.photoassignment.presentation.model.JournalData
import java.util.UUID

object TestData {
    fun createTestJournal(
        id: Long = UUID.randomUUID().mostSignificantBits,
        videoPath: String = "/test/path/${UUID.randomUUID()}",
        description: String? = "Test Description ${System.currentTimeMillis()}"
    ) = JournalData(
        id = id,
        videoUri = null, // Mocked in unit tests
        videoPath = videoPath,
        description = description,
        timestamp = System.currentTimeMillis()
    )

    val sampleJournals = listOf(
        createTestJournal(id = 1, description = "First entry"),
        createTestJournal(id = 2, description = "Second entry")
    )
}