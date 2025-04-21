package dev.stormery.photoassignment.domain.usecase

import dev.stormery.photoassignment.domain.repository.JournalRepository
import dev.stormery.photoassignment.presentation.model.JournalData

class DeleteJournalUseCase(
    private val repository: JournalRepository
) {
    suspend operator fun invoke(journal: JournalData) {
        repository.deleteJournal(journal)
    }
}