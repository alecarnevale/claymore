plugins {
  alias(libs.plugins.kotlin) apply false
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.hilt) apply false
  alias(libs.plugins.ksp) apply false
  id("claymore-dependency") apply false
}

allprojects {
  pluginManager.withPlugin(rootProject.libs.plugins.kotlin.asProvider().get().pluginId) {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
      kotlinOptions {
        allWarningsAsErrors = true
      }
    }
  }
  pluginManager.withPlugin(rootProject.libs.plugins.kotlin.android.get().pluginId) {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
      kotlinOptions {
        allWarningsAsErrors = true
      }
    }
  }
}