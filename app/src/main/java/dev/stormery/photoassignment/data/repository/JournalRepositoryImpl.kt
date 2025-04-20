package dev.stormery.photoassignment.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.stormery.photoassignment.database.JournalDatabase
import dev.stormery.photoassignment.database.JournalEntity
import dev.stormery.photoassignment.domain.repository.JournalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class JournalRepositoryImpl(
    db: JournalDatabase
):JournalRepository {

    private val queries = db.journalEntityQueries

    override fun getJournals(): Flow<List<JournalEntity>> {
        return queries.selectAll().asFlow().mapToList(
            context = Dispatchers.IO
        ).map { list ->
            list.map {
                JournalEntity(
                    id = it.id,
                    file_path = it.file_path,
                    description = it.description,
                    timestamp = it.timestamp
                )
            }
        }
    }

    override suspend fun insertJournal(filePath: String, description: String?, timestamp: Long) {
        return withContext(Dispatchers.IO){
            queries.insertVideo(
                file_path = filePath,
                description = description,
                timestamp = timestamp
            )
        }
    }

}