package dev.stormery.photoassignment.domain.repository

import dev.stormery.photoassignment.database.JournalEntity
import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    fun getJournals(): Flow<List<JournalEntity>>
    suspend fun insertJournal(filePath:String, description: String?, timestamp: Long)
}