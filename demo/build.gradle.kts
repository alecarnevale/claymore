plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("org.jetbrains.kotlin.kapt")
  id("com.google.dagger.hilt.android")
  id("com.google.devtools.ksp")
}

android {
  namespace = "com.alessandro.claymore.demo"
  compileSdk = 33

  defaultConfig {
    applicationId = "com.alessandro.claymore.demo"
    minSdk = 23
    targetSdk = 33
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.4.7"
  }
  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
  }
}

dependencies {
  implementation("androidx.appcompat:appcompat:1.6.1")

  implementation("androidx.compose.foundation:foundation:1.4.3")
  implementation("androidx.compose.material:material:1.4.3")
  implementation("androidx.compose.ui:ui:1.4.3")
  implementation("androidx.activity:activity-compose:1.7.1")
  implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
  debugImplementation("androidx.compose.ui:ui-tooling:1.4.3")

  implementation("com.google.dagger:hilt-android:2.46.1")
  kapt("com.google.dagger:hilt-compiler:2.46.1")

  testImplementation("junit:junit:4.13.2")
  testImplementation("org.robolectric:robolectric:4.10.3")
  testImplementation("androidx.compose.ui:ui-test-junit4:1.4.3")
  debugImplementation("androidx.compose.ui:ui-test-manifest:1.4.3")

  // claymore library dependency added from maven central (not local)
  compileOnly("io.github.alecarnevale:claymore-annotations:1.3.0")
  ksp("io.github.alecarnevale:claymore-processors:1.3.1")

  // only for developing phase, remember comment previous
  // compileOnly(project(":annotations"))
  // ksp(project(":processors"))
}