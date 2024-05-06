plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "vn.edu.tdc.xifood"
    compileSdk = 34

    defaultConfig {
        applicationId = "vn.edu.tdc.xifood"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.gms:google-services:4.4.1")
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.squareup.picasso:picasso:2.71828")
}