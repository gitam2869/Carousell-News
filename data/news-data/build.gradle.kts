plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "com.app.carousell.newsdata"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
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
        buildConfig = true
    }
    testOptions.unitTests {
        isIncludeAndroidResources = true
    }
}

dependencies {
    implementation(project(":common"))

    //Dagger
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    //Retrofit
    implementation(libs.retrofit.adapter.rxjava3)
    implementation(libs.converter.gson)
    implementation(libs.retrofit)
    implementation(libs.okhttp.client)
    implementation(libs.okhttp.loggingInterceptor)

    //RxJava
    implementation(libs.rxandroid)
    implementation(libs.rxjava)


    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.junit)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}