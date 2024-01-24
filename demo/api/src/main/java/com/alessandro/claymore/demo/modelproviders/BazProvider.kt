package com.alessandro.claymore.demo.modelproviders

import com.alessandro.claymore.demo.models.Baz

interface BazProvider {
  fun get(): Baz
}