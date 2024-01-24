package com.alessandro.claymore.demo.modelproviders

import com.alessandro.claymore.demo.models.Bar

interface BarProvider {
  fun get(): Bar
}