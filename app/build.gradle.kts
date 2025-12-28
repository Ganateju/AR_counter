plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // REMOVED: alias(libs.plugins.kotlin.compose) - This prevents UI conflicts
}

android {
    namespace = "com.example.mobileparallel"

    // Modern Target 36 Configuration
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.mobileparallel"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        // ESSENTIAL: Must be false for OpenCV JavaCameraView to render correctly
        compose = false
        // OPTIONAL: Set to true if you want to use ViewBinding instead of findViewById
        viewBinding = false
    }
}

dependencies {
    // 1. AI & COMPUTER VISION
    implementation("com.google.mlkit:object-detection:17.0.0")
    implementation(project(":opencv"))

    // 2. CORE UI (AppCompat for XML layouts)
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // 3. TESTING
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // REMOVED ALL COMPOSE DEPENDENCIES TO PREVENT BUILD ERRORS
}