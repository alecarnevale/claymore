package com.alessandro.claymore.demo.annotations.services.impl

import com.alecarnevale.claymore.annotations.AutoBinds
import com.alessandro.claymore.demo.annotations.CustomQualifierActivity
import com.alessandro.claymore.demo.annotations.models.NinjaTurtle
import com.alessandro.claymore.demo.annotations.services.ProvideWeapon
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@AutoBinds(component = ActivityComponent::class, annotations = [CustomQualifierActivity.DonatelloWeaponQualifier::class])
internal class ProvideDonatelloWeapon @Inject constructor() : ProvideWeapon {
  override fun invoke(): String = NinjaTurtle.Weapon.Staff.toString()
}