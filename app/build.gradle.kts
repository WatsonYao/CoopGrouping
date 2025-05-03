plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "watson.coopgrouping"
  compileSdk = 34

  defaultConfig {
    applicationId = "watson.coopgrouping"
    minSdk = 26
    targetSdk = 34
    versionCode = 8
    versionName = "1.3.2"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
  buildFeatures {
    viewBinding = true
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.navigation.fragment.ktx)
  implementation(libs.androidx.navigation.ui.ktx)

  implementation("androidx.recyclerview:recyclerview:1.3.2")
  implementation("com.squareup.retrofit2:retrofit:2.11.0")
  implementation("com.squareup.retrofit2:converter-gson:2.11.0")
  implementation("com.google.code.gson:gson:2.11.0")

  implementation("com.github.bumptech.glide:glide:4.16.0")
  implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

  annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
}