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
include(":demo:app")
include(":demo:impl")
include(":demo:multibindings")
