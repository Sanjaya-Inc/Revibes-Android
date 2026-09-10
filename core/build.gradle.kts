import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(sjy.plugins.buildlogic.lib)
    alias(sjy.plugins.buildlogic.compose)
    alias(sjy.plugins.buildlogic.firebase)
    alias(sjy.plugins.lumo)
    alias(sjy.plugins.buildlogic.detekt)
    alias(sjy.plugins.buildlogic.test)
}

val localProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

android {
    namespace = "com.carissa.revibes.core"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "BASE_URL",
            "${localProperties.getProperty("BASE_URL")}"
        )
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(sjy.androidx.lifecycle.runtime.ktx)
    api(sjy.bundles.orbit.mvi)

    debugImplementation(sjy.chucker)
    releaseImplementation(sjy.chucker.no.op)

    implementation(platform(sjy.firebase.bom))
    implementation(sjy.firebase.messaging)

//    testApi(libs.junit)
//    androidTestApi(libs.androidx.junit)
//    androidTestApi(libs.androidx.espresso.core)
}
