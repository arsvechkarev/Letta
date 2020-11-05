package com.arsvechkarev.letta.features.common

import android.graphics.Bitmap
import com.arsvechkarev.letta.core.model.Project

interface ProjectsRepository {
  
  fun hasMoreProjects(currentIndex: Int): Boolean
  
  fun getProjects(fromIndex: Int, limit: Int): List<Project>
  
  fun loadLatestProject(filename: String): Project
  
  fun createProject(bitmap: Bitmap)
  
  fun deleteProjects(projects: Iterable<Project>)
}