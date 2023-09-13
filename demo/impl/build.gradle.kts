plugins {
  alias(libs.plugins.kotlin)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kapt)
  id("claymore-dependency")
}

dependencies {
  api(project(":demo:api"))
  compileOnly(libs.javax.inject)

  implementation(libs.hilt.core)
  kapt(libs.hilt.compiler)
}