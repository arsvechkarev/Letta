package com.arsvechkarev.letta.core

import android.os.Bundle

interface Navigator {
  
  fun openProject(arguments: Bundle)
  
  fun popBackStack()
}