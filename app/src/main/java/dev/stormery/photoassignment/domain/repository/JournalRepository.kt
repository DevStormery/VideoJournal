package dev.stormery.photoassignment.domain.repository

import dev.stormery.photoassignment.database.JournalEntity
import dev.stormery.photoassignment.presentation.model.JournalData
import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    fun getJournals(): Flow<List<JournalData>>
    suspend fun insertJournal(filePath:String, videoPath:String, description: String?, timestamp: Long)
    suspend fun deleteJournal(journal: JournalData)
}