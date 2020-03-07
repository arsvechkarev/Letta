package com.arsvechkarev.letta.utils.extenstions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import com.arsvechkarev.letta.views.ListenableConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams as ConstraintParams

fun View.visible() {
  visibility = View.VISIBLE
}

fun View.invisible() {
  visibility = View.INVISIBLE
}

fun View.gone() {
  visibility = View.GONE
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
  return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ViewGroup.inflateContainer(@LayoutRes layoutRes: Int): ListenableConstraintLayout {
  return LayoutInflater.from(context).inflate(layoutRes, this, false) as ListenableConstraintLayout
}

fun View.constraints(block: ConstraintParams.() -> Unit) {
  layoutParams = ConstraintParams(layoutParams as ConstraintParams).apply(block)
}

fun View.layoutParams(block: MarginLayoutParams.() -> Unit) {
  layoutParams = MarginLayoutParams(layoutParams as MarginLayoutParams).apply(block)
}

fun View.toggleKeyboard() {
  val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.toggleSoftInput(0, 0)
}