package com.arsvechkarev.letta.extensions

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <T> T?.ifNotNull(block: (T) -> Unit) {
  contract {
    callsInPlace(block, InvocationKind.AT_MOST_ONCE)
  }
  this?.apply(block)
}

@OptIn(ExperimentalContracts::class)
inline fun <reified T> Any?.ifTypeOf(block: (T) -> Unit) {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  if (this is T) this.apply(block)
}

inline fun <T> List<T>.forEachReversed(block: (T) -> Unit) {
  for (i in size - 1 downTo 0) {
    block(get(i))
  }
}

inline fun <T> Array<T>.forEachReversed(block: (T) -> Unit) {
  for (i in size - 1 downTo 0) {
    block(get(i))
  }
}