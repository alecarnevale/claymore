plugins {
  id("org.jetbrains.kotlin.jvm")
  id("com.google.devtools.ksp")
}

dependencies {
  api(project(":demo:api"))
  compileOnly("javax.inject:javax.inject:1")

  // claymore library dependency added from maven central (not local)
  compileOnly("io.github.alecarnevale:claymore-annotations:1.3.1")
  ksp("io.github.alecarnevale:claymore-processors:1.3.1")

  // only for developing phase, remember comment previous
  // compileOnly(project(":annotations"))
  // ksp(project(":processors"))
}