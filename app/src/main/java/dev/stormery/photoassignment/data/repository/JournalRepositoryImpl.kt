package dev.stormery.photoassignment.data.repository

import androidx.core.net.toUri
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.stormery.photoassignment.database.JournalDatabase
import dev.stormery.photoassignment.database.JournalEntity
import dev.stormery.photoassignment.domain.repository.JournalRepository
import dev.stormery.photoassignment.presentation.model.JournalData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class JournalRepositoryImpl(
    db: JournalDatabase
):JournalRepository {

    private val queries = db.journalEntityQueries

    override fun getJournals(): Flow<List<JournalData>> {
        return queries.selectAll().asFlow().mapToList(
            context = Dispatchers.IO
        ).map { list ->
            list.map {
                JournalData(
                    id = it.id,
                    videoUri = it.file_path.toUri(),
                    videoPath = it.videoPath,
                    description = it.description,
                    timestamp = it.timestamp
                )
            }.sortedByDescending { it.timestamp }
        }
    }

    override suspend fun insertJournal(filePath: String, videoPath:String,description: String?, timestamp: Long) {
        return withContext(Dispatchers.IO){
            queries.insertVideo(
                file_path = filePath,
                videoPath = videoPath,
                description = description,
                timestamp = timestamp,

            )
        }
    }

    override suspend fun deleteJournal(journal: JournalData) {
        return withContext(Dispatchers.IO) {
            queries.deleteVideoById(journal.id)
        }
    }

}