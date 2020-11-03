package com.arsvechkarev.letta.extensions

import com.arsvechkarev.letta.BuildConfig
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun assertThat(condition: Boolean, lazyMessage: () -> String = { "" }) {
  contract {
    callsInPlace(lazyMessage, InvocationKind.EXACTLY_ONCE)
    returns() implies condition
  }
  if (BuildConfig.DEBUG) {
    if (!condition) {
      throw AssertionError(lazyMessage())
    }
  }
}

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

fun throwEx(): Nothing {
  throw IllegalStateException()
}

inline fun <T> Array<T>.forEachReversed(block: (T) -> Unit) {
  for (i in size - 1 downTo 0) {
    block(get(i))
  }
}