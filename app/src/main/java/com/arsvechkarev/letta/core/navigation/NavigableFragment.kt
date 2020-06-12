package com.arsvechkarev.letta.core.navigation

/**
 * Fragment that interested in navigation events, such as onBackPressed
 */
interface NavigableFragment {
  
  /**
   * Called when user presses back button and if this fragment is currently visible to user
   *
   * If returns true, then navigator is allowed to pop this fragment. If returns false, then
   * fragment is not popped from back stack
   */
  fun onBackPressed(): Boolean = true
}