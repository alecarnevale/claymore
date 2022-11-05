package com.alessandro.claymore.demo.modelproviders

import com.alessandro.claymore.demo.models.Foo

interface FooProvider {
  fun get(): Foo
}