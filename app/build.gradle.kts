import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

val configProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    configProperties.load(localPropertiesFile.inputStream())
}

val apiToken = configProperties.getProperty("api.token") ?: "API token not found in local.properties. Please define 'api.token"

android {
    namespace = "com.jeksonsoftsolutions.cryptos"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jeksonsoftsolutions.cryptos"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_TOKEN", "\"${apiToken}\"")
    }

    val keystorePropertiesFile = rootProject.file("keystore.properties")
    val keystoreProperties = Properties().apply {
        load(FileInputStream(keystorePropertiesFile))
    }
    signingConfigs {
        getByName("debug") {
            storeFile = file(keystoreProperties.getProperty("keystore.path"))
            storePassword = keystoreProperties.getProperty("keystore.password")
            keyAlias = keystoreProperties.getProperty("keystore.key.alias")
            keyPassword = keystoreProperties.getProperty("keystore.key.password")
        }
        create("release") {
            storeFile = file(keystoreProperties.getProperty("keystore.path"))
            storePassword = keystoreProperties.getProperty("keystore.password")
            keyAlias = keystoreProperties.getProperty("keystore.key.alias")
            keyPassword = keystoreProperties.getProperty("keystore.key.password")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
        }
        debug {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
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
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
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

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.navigation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.graphics.shapes)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.kotlinx.collections.immutable)

    // Material Icons Extended
    implementation(libs.androidx.material.icons.extended)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // coil
    implementation(libs.coil.compose)

    // room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

}