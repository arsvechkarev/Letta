package com.arsvechkarev.letta.features.common

import android.graphics.Bitmap
import com.arsvechkarev.letta.core.model.Project
import comarsvechkarevletta.ProjectsDatabaseQueries
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ProjectsSqliteRepository(
  private val database: ProjectsDatabaseQueries,
  private val projectFileStorage: ProjectFileStorage
) : ProjectsRepository {
  
  override fun hasMoreProjects(currentIndex: Int): Boolean {
    return currentIndex < getNumberOfProjects()
  }
  
  override fun getProjects(fromIndex: Int, limit: Int): List<Project> {
    val offset = minOf(fromIndex, getNumberOfProjects() - 1)
    return database.fetchProjects(limit.toLong(), offset.toLong()) { _id, filePath, _ ->
      projectFileStorage.getProject(_id, filePath)
    }.executeAsList()
  }
  
  override fun loadLatestProject(filename: String): Project {
    val id = database.getLastInsertedProjectId().executeAsOne()
    return projectFileStorage.getProject(id, filename)
  }
  
  override fun createProject(bitmap: Bitmap) {
    val dateFormat = SimpleDateFormat("yyyy_MM_dd--HH_mm_ss--", Locale.getDefault())
    val timestamp = dateFormat.format(Date())
    val randomizedTimeStamp = timestamp + UUID.randomUUID().leastSignificantBits.toString()
    val projectFilename = "Project_$randomizedTimeStamp.png"
    Timber.d("Saving project: $projectFilename")
    database.createProject(projectFilename, timestamp)
    projectFileStorage.createProject(projectFilename, bitmap)
  }
  
  override fun deleteProjects(projects: Iterable<Project>) {
    projects.forEach { project ->
      database.deleteProject(project.idLong)
      projectFileStorage.deleteProject(project.filename)
    }
  }
  
  private fun getNumberOfProjects() = database.getNumberOfProjects().executeAsOne().toInt()
}