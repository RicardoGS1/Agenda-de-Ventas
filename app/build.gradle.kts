plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("com.google.devtools.ksp")
    id ("com.google.dagger.hilt.android")
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.virtualworld.agendadeventas"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.virtualworld.agendadeventas"
        minSdk = 27
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

    //kotlin
    implementation(libs.androidx.core.ktx)

    //compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation (libs.androidx.material)
    implementation (libs.androidx.material.icons.extended)

    //CORRUTINA
    implementation (libs.androidx.lifecycle.runtime.ktx)
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.androidx.activity.ktx)
    implementation(libs.androidx.runtime.livedata)

    //NAVEGATION
    implementation ("androidx.navigation:navigation-compose:2.7.7")

    //Dagger hilt
    implementation ("javax.annotation:javax.annotation-api:1.3.2")
    implementation ("com.google.dagger:hilt-android:2.49")

    ksp ("com.google.dagger:hilt-compiler:2.47")
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")

    //ROOM
    implementation ("androidx.room:room-runtime:2.6.1")
    ksp ("androidx.room:room-compiler:2.6.1")

    //FIREBASE
    implementation ("com.google.firebase:firebase-bom:33.1.2")
    //implementation ("com.google.firebase:firebase-analytics-ktx")
    implementation ("com.google.firebase:firebase-auth-ktx:23.0.0")
    implementation ("com.google.firebase:firebase-firestore-ktx:25.0.0")


    //testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}