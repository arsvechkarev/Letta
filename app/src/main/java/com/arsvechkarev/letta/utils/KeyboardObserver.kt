package com.arsvechkarev.letta.utils

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * Allows to observe when keyboard is opened/closed
 *
 * @param view root view of the activity
 * @param onOpened action when keyboard is opened
 * @param onClosed action when keyboard is closed
 *
 * @see addKeyboardObserver
 */
class KeyboardObserver(
  private val view: View,
  private val onOpened: () -> Unit,
  private val onClosed: () -> Unit
) : LifecycleObserver {
  
  private var isOpenedCurrently = false
  
  // threshold for checking the diff
  private val threshold = view.resources.displayMetrics.density * 50
  
  private val checkKeyboardListener = ViewTreeObserver.OnGlobalLayoutListener {
    val viewBounds = Rect()
    view.getWindowVisibleDisplayFrame(viewBounds)
    val heightDiff = view.height - viewBounds.height()
    val shouldBeOpened = heightDiff > threshold
    if (shouldBeOpened == isOpenedCurrently) return@OnGlobalLayoutListener
    isOpenedCurrently = shouldBeOpened
    if (shouldBeOpened) {
      onOpened()
    } else {
      onClosed()
    }
  }
  
  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  fun onStart() {
    view.viewTreeObserver.addOnGlobalLayoutListener(checkKeyboardListener)
  }
  
  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  fun onStop() {
    view.viewTreeObserver.removeOnGlobalLayoutListener(checkKeyboardListener)
  }
  
}

/**
 * Helper method that adds keyboard observer to the activity
 *
 * @param idRes root view of the activity
 * @param onOpened action when keyboard is opened
 * @param onClosed action when keyboard is closed
 */
fun ComponentActivity.addKeyboardObserver(
  @IdRes idRes: Int,
  onOpened: () -> Unit = {},
  onClosed: () -> Unit = {}
) {
  this.lifecycle.addObserver(KeyboardObserver(findViewById(idRes), onOpened, onClosed))
}

fun Fragment.addKeyboardObserver(
  view: View,
  onOpened: () -> Unit = {},
  onClosed: () -> Unit = {}
) {
  this.lifecycle.addObserver(KeyboardObserver(view, onOpened, onClosed))
}
