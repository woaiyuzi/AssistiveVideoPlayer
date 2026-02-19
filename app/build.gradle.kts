import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
}

val keystoreProperties = Properties()
val keystorePropertiesFile = rootProject.file("local.properties")
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    namespace = "love.yuzi.avp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "love.yuzi.avp"
        minSdk = 32
        targetSdk = 36
        versionCode = 100
        versionName = "1.0.0"

        ndk {
            abiFilters.add("arm64-v8a")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions.add("version")

    productFlavors {
        create("short_drama") {
            dimension = "version"
            applicationId = "love.yuzi.avp.shortdrama"
        }

        create("long_video") {
            dimension = "version"
            applicationId = "love.yuzi.avp.longvideo"
        }

        create("folksong") {
            dimension = "version"
            applicationId = "love.yuzi.avp.folksong"
        }
    }

    signingConfigs {
        create("release") {
            val sFile = keystoreProperties.getProperty("signing.storeFile")
            if (sFile != null) {
                storeFile = file(sFile)
                storePassword = keystoreProperties.getProperty("signing.storePassword")
                keyAlias = keystoreProperties.getProperty("signing.keyAlias")
                keyPassword = keystoreProperties.getProperty("signing.keyPassword")

                enableV1Signing = true
                enableV2Signing = true
                enableV3Signing = true
                enableV4Signing = false
            } else {
                println("signing.storeFile is null")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
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

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.timber)
    implementation(libs.icons.extended)
    implementation(libs.coil.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.androidx.datastore.preferences)

    implementation(project(":video-data"))
    implementation(project(":video-resource"))
    implementation(project(":logger"))
    implementation(project(":ui-common"))
    implementation(project(":base"))

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    debugImplementation(libs.leakcanary)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}