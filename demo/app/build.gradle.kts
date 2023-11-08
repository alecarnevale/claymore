plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kapt)
  alias(libs.plugins.hilt)
  alias(libs.plugins.ksp)
  id("claymore-dependency")
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

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
  }
  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
    managedDevices {
      devices {
        maybeCreate<com.android.build.api.dsl.ManagedVirtualDevice>("nexusOneApi30").apply {
          // Use device profiles you typically see in Android Studio.
          device = "Nexus One"
          // Use only API levels 27 and higher.
          apiLevel = 30
          // To include Google services, use "google".
          systemImageSource = "aosp-atd"
        }
      }
    }
  }
}

dependencies {
  implementation(project(":demo:impl"))
  implementation(project(":demo:multibindings"))

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

  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
}