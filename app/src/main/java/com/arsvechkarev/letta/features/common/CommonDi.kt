package com.arsvechkarev.letta.features.common

import com.arsvechkarev.letta.Database
import com.arsvechkarev.letta.LettaApplication
import com.arsvechkarev.letta.core.DATABASE_NAME
import com.arsvechkarev.letta.extensions.projectsDirectory
import com.squareup.sqldelight.android.AndroidSqliteDriver

object CommonDi {
  
  private var projectsRepository: ProjectsRepository? = null
  
  fun provideProjectRepository(): ProjectsRepository {
    if (projectsRepository == null) {
      val context = LettaApplication.appContext
      val driver = AndroidSqliteDriver(Database.Schema, context, DATABASE_NAME)
      val queries = Database(driver).projectsDatabaseQueries
      val fileStorage = ProjectFileStorage(context.projectsDirectory)
      projectsRepository = ProjectsSqliteRepository(queries, fileStorage)
    }
    return projectsRepository!!
  }
}