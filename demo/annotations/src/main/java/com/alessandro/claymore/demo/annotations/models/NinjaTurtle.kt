package com.alessandro.claymore.demo.annotations.models

internal data class NinjaTurtle(
  val name: String,
  val bandana: Color,
  val weapon: Weapon
) {
  enum class Color {
    BLUE, RED, PURPLE, ORANGE
  }

  sealed class Weapon {
    data object Katana: Weapon()
    data object Sai: Weapon()
    data object Staff: Weapon()
    data object Nunchaku: Weapon()
  }
}