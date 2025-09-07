plugins {
    alias(libs.plugins.sjy.lib)
    alias(libs.plugins.sjy.compose)
}

android {
    namespace = "com.carissa.revibes.manage_transaction"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    implementation(project(":core"))
    implementation(project(":features:transaction-history"))
    implementation(sjy.accompanist.permissions)
}
