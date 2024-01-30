plugins {
  alias(libs.plugins.kotlin)
  alias(libs.plugins.ksp)
  id("claymore-dependency")
}

dependencies {
  api(project(":demo:api"))
  compileOnly(libs.javax.inject)

  implementation(libs.hilt.core)
  ksp(libs.hilt.compiler)
}