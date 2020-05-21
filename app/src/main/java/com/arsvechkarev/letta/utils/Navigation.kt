package com.arsvechkarev.letta.utils

import androidx.fragment.app.Fragment
import com.arsvechkarev.letta.MainActivity
import com.arsvechkarev.letta.core.Navigator

val Fragment.navigator: Navigator get() = (requireActivity() as MainActivity).navigator