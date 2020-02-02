package com.arsvechkarev.letta.utils

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment

const val TAG = "CameraM"

fun Fragment.log(text: String) {
  Log.d(TAG, text)
}

fun Fragment.showToast(text: String) {
  Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}
