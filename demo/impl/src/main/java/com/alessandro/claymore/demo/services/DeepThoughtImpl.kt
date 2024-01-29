package com.alessandro.claymore.demo.services

import com.alecarnevale.claymore.annotations.AutoBinds
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@AutoBinds(component = SingletonComponent::class)
internal class DeepThoughtImpl @Inject constructor() : DeepThought {
  override fun getAnswer(): String {
    return "42"
  }
}