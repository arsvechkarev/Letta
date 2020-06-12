package com.arsvechkarev.letta.core.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.arsvechkarev.letta.MainActivity

interface Navigator {
  
  fun openProject(arguments: Bundle)
  
  fun popBackStack()
}

val Fragment.navigator: Navigator get() = (requireActivity() as MainActivity).navigator