plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)

}

android {
    namespace = "com.example.intrior_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.intrior_app"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Use compatible versions for androidx.core and other dependencies.
    implementation("androidx.core:core-ktx:1.12.0") // Downgraded version
    implementation("androidx.appcompat:appcompat:1.6.1") // Confirm compatibility with SDK 34

    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.ktx)
    implementation(libs.play.services.analytics.impl)
    implementation(libs.play.services.location)
    kapt(libs.androidx.room.compiler)

    // Retrofit dependencies
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("androidx.room:room-runtime:2.5.1") // Check for the latest version
    kapt ("androidx.room:room-compiler:2.5.1") // If you're using Kotlin
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.0") // For LiveData
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0") // For ViewModel (if used)

    implementation ("com.squareup.picasso:picasso:2.71828")
    // Glide dependencies
    implementation("com.github.bumptech.glide:glide:4.15.0")
    kapt("com.github.bumptech.glide:compiler:4.15.0") // For Kotlin users

    implementation ("com.google.android.material:material:1.9.0")

}