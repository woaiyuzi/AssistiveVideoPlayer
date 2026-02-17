plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "love.yuzi.logger"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 32

        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    api(libs.timber)
    implementation(libs.junit)
}