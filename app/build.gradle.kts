plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.mrboomdev.gallery"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mrboomdev.gallery"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
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
}

dependencies {
    // Core
    implementation(libs.annotations)
    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.activity)
    implementation(libs.fragment.ktx)

    // Ui
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.core.splashscreen)
    implementation(libs.balloon)

    // Image Viewers
    implementation(libs.coil)
    implementation(libs.coil.gif)
    implementation(libs.coil.svg)
    implementation(libs.coil.video)
    implementation(libs.bigimageviewer)
    implementation(libs.frescoimageloader)
    implementation(libs.frescoimageviewfactory)

    // Data
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
}