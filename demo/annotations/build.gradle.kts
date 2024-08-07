plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose.compiler)
  alias(libs.plugins.hilt)
  alias(libs.plugins.ksp)
  id("claymore-dependency")
}

android {
  namespace = "com.alessandro.claymore.demo.annotations"
  compileSdk = 34

  defaultConfig {
    minSdk = 23
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
  @Suppress("UnstableApiUsage")
  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
  }
}

dependencies {
  compileOnly(libs.javax.inject)

  implementation(libs.androidx.appcompat)

  implementation(libs.compose.foundation)
  implementation(libs.compose.material)
  implementation(libs.compose.ui)
  implementation(libs.androidx.activity.compose)
  implementation(libs.compose.ui.tooling.preview)
  debugImplementation(libs.compose.ui.tooling)

  implementation(libs.hilt.android)
  ksp(libs.hilt.compiler)

  testImplementation(libs.junit)
  testImplementation(libs.robolectric)
  testImplementation(libs.compose.ui.test.junit4)
  debugImplementation(libs.compose.ui.test.manifest)

  testImplementation(libs.hilt.testing)
  kspTest(libs.hilt.compiler)
}