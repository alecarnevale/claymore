pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "claymore"
include(":annotations")
include(":processors")
include(":demo:api")
include(":demo:impl")
include(":demo:app")
