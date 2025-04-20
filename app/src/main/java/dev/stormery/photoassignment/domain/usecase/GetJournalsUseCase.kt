package dev.stormery.photoassignment.domain.usecase

import dev.stormery.photoassignment.domain.repository.JournalRepository

class GetJournalsUseCase(
    private val repository: JournalRepository
) {
    operator fun invoke() = repository.getJournals()
}