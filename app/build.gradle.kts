import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.safebyte"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.safebyte"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Set value part
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"${properties.getProperty("SUPABASE_ANON_KEY")}\"")
        buildConfigField("String", "SECRET", "\"${properties.getProperty("SECRET")}\"")
        buildConfigField("String", "SUPABASE_URL", "\"${properties.getProperty("SUPABASE_URL")}\"")
    }

//    buildTypes {
//        val localProperties = Properties()
//        val localPropertiesFile = rootProject.file("local.properties")
//        if (localPropertiesFile.exists()) {
//            localProperties.load(localPropertiesFile.inputStream())
//        }
//
//        debug {
//            val supabaseUrl = localProperties.getProperty("SUPABASE_URL", "")
//            val supabaseKey = localProperties.getProperty("SUPABASE_KEY", "")
//
//            buildConfigField("String", "SUPABASE_URL", "\"$supabaseUrl\"")
//            buildConfigField("String", "SUPABASE_KEY", "\"$supabaseKey\"")
//        }
//
//        release {
//            val supabaseUrl: String? = System.getenv("SUPABASE_URL")
//            val supabaseKey: String? = System.getenv("SUPABASE_KEY")
//
//            buildConfigField("String", "SUPABASE_URL", "\"${supabaseUrl ?: ""}\"")
//            buildConfigField("String", "SUPABASE_KEY", "\"${supabaseKey ?: ""}\"")
//        }
//    }

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
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.google.accompanist.navigation.animation)

    // Retrofit
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    // datastore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.play.services.auth)

    // Supabase
    implementation (libs.postgrest.kt.v311)
    implementation (libs.storage.kt)
    implementation (libs.auth.kt)

    // Ktor
    implementation (libs.ktor.client.android)
    implementation (libs.ktor.client.core)
    implementation (libs.ktor.utils)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.plugins)


    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)

    // coil for async image
    implementation(libs.coil.compose)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)

    // firebase storage
    implementation(libs.firebase.storage.ktx)

    // google fonts
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.runtime)

    // to navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.ui)
    implementation(libs.androidx.material)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.firebase.vertexai)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // ZXing para gerar QR Codes
    implementation ("com.google.zxing:core:3.5.0")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation ("androidx.camera:camera-core:1.2.2")
    implementation ("androidx.camera:camera-camera2:1.2.2")
    implementation ("androidx.camera:camera-lifecycle:1.2.2")
    implementation ("androidx.camera:camera-view:1.2.2")
    implementation ("com.google.accompanist:accompanist-permissions:0.28.0")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

}
