package com.arsvechkarev.letta.core

import com.arsvechkarev.letta.core.model.Project

interface Sharer {
  
  fun share(projects: Collection<Project>)
}