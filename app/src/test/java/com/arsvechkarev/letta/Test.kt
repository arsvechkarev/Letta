package com.arsvechkarev.letta

import org.junit.Test

class Test {
  
  @Test
  fun test() {
    val what: Parent = Parent()
    println(what::class.java.simpleName)
  }
  
  open class Parent
  class Child : Parent()
}