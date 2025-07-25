plugins {
    alias(libs.plugins.sjy.app)
    alias(libs.plugins.sjy.compose)
    alias(libs.plugins.sjy.firebase)
}

android {
    namespace = "com.carissa.revibes"

    defaultConfig {
        applicationId = "com.carissa.revibes"
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()
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
}

dependencies {
    implementation(project(":core"))
    implementation(project(":features:auth"))
    implementation(project(":features:onboarding"))
    implementation(project(":features:home"))
    implementation(project(":features:profile"))
    implementation(project(":features:transaction-history"))
    implementation(project(":features:shop"))
    implementation(project(":features:exchange-points"))
    implementation(project(":features:drop-off"))
    implementation(project(":features:help-center"))
    implementation(project(":features:admin-menu"))
    implementation(project(":features:home-admin"))
    implementation(project(":features:manage-users"))
    implementation(project(":features:manage-transaction"))
    implementation(project(":features:manage-voucher"))
    implementation(project(":features:pick-up"))
    implementation(project(":features:point"))
}