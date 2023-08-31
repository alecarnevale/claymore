plugins {
  alias(libs.plugins.kotlin)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kapt)
}

dependencies {
  api(project(":demo:api"))
  compileOnly(libs.javax.inject)

  implementation(libs.hilt.core)
  kapt(libs.hilt.compiler)

  // claymore library dependency added from maven central (not local)
  compileOnly("io.github.alecarnevale:claymore-annotations:1.3.2")
  ksp("io.github.alecarnevale:claymore-processors:1.3.2")

  // only for developing phase, remember comment previous
  // compileOnly(project(":annotations"))
  // ksp(project(":processors"))
}