plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.hegemonycompose"
    compileSdk = 35
    flavorDimensions += listOf("database")

    defaultConfig {
        applicationId = "com.example.hegemonycompose"
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("debug")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    productFlavors {
        create("localDbMinSdk33") {
            dimension = "database"
            buildConfigField("String", "DB_TYPE", "\"LOCAL\"")
            minSdk = 33
        }
        create("localDbMinSdk26") {
            dimension = "database"
            buildConfigField("String", "DB_TYPE", "\"LOCAL\"")
            minSdk = 26
        }
        create("firebaseDbMinSdk33") {
            dimension = "database"
            buildConfigField("String", "DB_TYPE", "\"FIREBASE\"")
            minSdk = 33
        }
        create("firebaseDbMinSdk26") {
            dimension = "database"
            buildConfigField("String", "DB_TYPE", "\"FIREBASE\"")
            minSdk = 26
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.hilt.android)
    implementation(libs.androidx.adaptive.android)
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.database.ktx)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.constraintlayout.compose)

    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.room.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(platform(libs.google.firebase.bom))
    implementation(libs.firebase.database)
}

kapt {
    correctErrorTypes = true
}