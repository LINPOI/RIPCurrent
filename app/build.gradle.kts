plugins {
 //   id("com.android.application")
    id("com.google.gms.google-services")
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.ripcurrent"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ripcurrent"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
//    //google
//    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
//    implementation("com.google.firebase:firebase-analytics")
//    implementation ("com.firebaseui:firebase-ui-auth:7.2.0")
//    // Import the BoM for the Firebase platform
//    // Add the dependency for the Firebase Authentication library
//    // When using the BoM, you don't specify versions in Firebase library dependencies
//    implementation("com.google.firebase:firebase-auth")
//
//    // Also add the dependency for the Google Play services library and specify its version
//    implementation("com.google.android.gms:play-services-auth:21.0.0")
//    //facebook
//    implementation ("com.facebook.android:facebook-android-sdk:8.x")
    //google location
    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation("com.google.accompanist:accompanist-permissions:0.35.0-alpha")
    //google map
    implementation ("com.google.maps.android:maps-compose:2.11.4")
    implementation ("com.google.android.gms:play-services-maps:18.1.0")
    implementation ("com.google.android.gms:play-services-location:21.2.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.0.1")
    // Moshi
    implementation("com.squareup.moshi:moshi:1.14.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    // OKHttp Interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    //繪圖用
    implementation ("io.coil-kt:coil-compose:2.5.0")
    implementation ("io.getstream:sketchbook:1.0.4")
    //分頁
    implementation(libs.androidx.navigation.compose)
    //儲存用
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation(libs.firebase.auth)
    implementation(libs.androidx.appcompat)
    //camera
    val cameraxVersion = "1.3.3"
    implementation ("androidx.camera:camera-core:${cameraxVersion}")
    implementation ("androidx.camera:camera-camera2:${cameraxVersion}")
    implementation ("androidx.camera:camera-view:${cameraxVersion}")
    implementation ("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}