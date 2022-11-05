plugins {
  id("com.android.application") version "7.3.0"
  id("org.jetbrains.kotlin.android") version "1.7.20"
  id("org.jetbrains.kotlin.kapt") version "1.7.20"
  id("com.google.dagger.hilt.android") version "2.44"
  id("com.google.devtools.ksp") version "1.7.20-1.0.8"
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
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.3.2"
  }
}

dependencies {
  implementation("com.google.dagger:hilt-android:2.44")
  kapt("com.google.dagger:hilt-compiler:2.44")
}