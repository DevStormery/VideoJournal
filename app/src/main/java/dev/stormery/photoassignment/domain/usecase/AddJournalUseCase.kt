package dev.stormery.photoassignment.domain.usecase

import dev.stormery.photoassignment.domain.repository.JournalRepository

class AddJournalUseCase(
    private val repository: JournalRepository
) {
    suspend operator fun invoke(filePath: String, description: String?, timestamp: Long) {
        repository.insertJournal(filePath, description, timestamp)
    }
}