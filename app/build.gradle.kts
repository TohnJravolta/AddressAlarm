plugins {
  id("com.android.application")
  // kotlin("android") // Replaced by org.jetbrains.kotlin.android from previous state
  id("org.jetbrains.kotlin.android")
  // kotlin("kapt") // Replaced by org.jetbrains.kotlin.kapt from previous state (or id("kotlin-kapt"))
  id("kotlin-kapt")
  id("org.jetbrains.kotlin.plugin.compose")
  id("org.jetbrains.kotlin.plugin.serialization")
}

android {
  namespace = "org.addressalarm"
  compileSdk = 35

  defaultConfig {
    applicationId = "org.addressalarm"
    minSdk = 26
    targetSdk = 35
    versionCode = 1
    versionName = "0.1.2-fixed"
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
    debug {
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-debug"
    }
  }

  buildFeatures {
    compose = true
    buildConfig = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = "17"
    // Corrected freeCompilerArgs to remove redundant Elvis operator
    freeCompilerArgs = freeCompilerArgs
      .filterNot { it.startsWith("-Xopt-in") }
      .plus(listOf("-opt-in=kotlin.RequiresOptIn"))
  }

  lint {
    // Use the lint.xml configuration file
    lintConfig = file("lint.xml")
    // Don't abort build on lint errors (warnings only)
    abortOnError = false
    // Check dependencies for issues
    checkDependencies = true
  }

  packaging {
    resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    resources.excludes += "/META-INF/DEPENDENCIES"
  }
}

dependencies {
  val roomVersion = "2.6.1" // Defined room version

  implementation("androidx.room:room-runtime:$roomVersion") // Added
  implementation("androidx.room:room-ktx:$roomVersion") // Ensured version
  kapt("androidx.room:room-compiler:$roomVersion") // Ensured version
  implementation("androidx.sqlite:sqlite-ktx:2.4.0")
  implementation("net.zetetic:android-database-sqlcipher:4.5.4")
  implementation("androidx.security:security-crypto:1.1.0-alpha06")
  implementation("com.google.http-client:google-http-client-gson:1.42.3")
  implementation("androidx.preference:preference-ktx:1.2.0")

  val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
  implementation(composeBom)
  androidTestImplementation(composeBom)

  implementation("androidx.core:core-ktx:1.13.1")
  implementation("androidx.appcompat:appcompat:1.7.0")
  implementation("androidx.activity:activity-compose:1.9.1")
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-tooling-preview")
  debugImplementation("androidx.compose.ui:ui-tooling")
  implementation("androidx.compose.material3:material3:1.3.0")
  implementation("androidx.compose.material:material-icons-extended")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
  implementation("androidx.navigation:navigation-compose:2.8.0-beta05")
  implementation("androidx.datastore:datastore-preferences:1.1.1")

  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
  implementation("com.google.android.material:material:1.12.0")

  androidTestImplementation("androidx.test:core:1.5.0")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
}

kapt {
  correctErrorTypes = true
}

// Ensure LICENSE from repo root is packaged inside the app install
// so we can display it offline in-app.
val copyLicense by tasks.register<Copy>("copyLicense") {
  from(rootProject.file("LICENSE"))
  into(layout.projectDirectory.dir("src/main/assets"))
  rename { "LICENSE" }
}

tasks.named("preBuild").configure {
  dependsOn(copyLicense)
}
