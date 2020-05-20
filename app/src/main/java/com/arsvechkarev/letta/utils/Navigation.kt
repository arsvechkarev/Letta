package com.arsvechkarev.letta.utils

import androidx.fragment.app.Fragment
import com.arsvechkarev.letta.MainActivity
import com.arsvechkarev.letta.core.Navigator

val Fragment.navigator get() = (requireActivity() as MainActivity).navigator