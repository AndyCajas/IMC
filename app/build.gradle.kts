plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "app.example.imc"
    compileSdk = 35

    defaultConfig {
        applicationId = "app.example.imc"
        minSdk = 27
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
    viewBinding{
        enable= true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //lottie
    implementation(libs.android.lottie)

    //glide
    implementation(libs.android.glide)

    //imagen
    implementation(libs.android.circle)

    //gson
    implementation(libs.android.gson)

    //implementation(libs.play.services)

    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation(libs.google.services)

    implementation("com.google.firebase:firebase-auth")

    implementation ("com.google.firebase:firebase-database-ktx:20.2.5")


}