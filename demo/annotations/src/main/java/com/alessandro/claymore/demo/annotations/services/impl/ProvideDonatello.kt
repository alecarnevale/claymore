package com.alessandro.claymore.demo.annotations.services.impl

import com.alecarnevale.claymore.annotations.AutoBinds
import com.alessandro.claymore.demo.annotations.models.NinjaTurtle
import com.alessandro.claymore.demo.annotations.services.ProvideNinjaTurtle
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoSet
import javax.inject.Inject

@AutoBinds(component = ActivityComponent::class, annotations = [IntoSet::class])
internal class ProvideDonatello @Inject constructor() : ProvideNinjaTurtle {
  override fun invoke() =
    NinjaTurtle(
      name = "Donatello",
      bandana = NinjaTurtle.Color.PURPLE,
      weapon = NinjaTurtle.Weapon.Staff
    )
}