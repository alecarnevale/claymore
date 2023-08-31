plugins {
  id("org.jetbrains.kotlin.jvm")
  id("com.google.devtools.ksp")
  id("org.jetbrains.kotlin.kapt")
}

dependencies {
  api(project(":demo:api"))
  compileOnly("javax.inject:javax.inject:1")

  implementation("com.google.dagger:hilt-core:2.46.1")
  kapt("com.google.dagger:hilt-compiler:2.48")

  // claymore library dependency added from maven central (not local)
  compileOnly("io.github.alecarnevale:claymore-annotations:1.3.2")
  ksp("io.github.alecarnevale:claymore-processors:1.3.2")

  // only for developing phase, remember comment previous
  // compileOnly(project(":annotations"))
  // ksp(project(":processors"))
}