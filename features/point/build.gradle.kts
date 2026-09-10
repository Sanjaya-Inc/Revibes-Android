plugins {
    id("com.sanjaya.buildlogic.lib")
    id("com.sanjaya.buildlogic.compose")
    alias(sjy.plugins.buildlogic.detekt)
    alias(sjy.plugins.buildlogic.test)
}

android {
    namespace = "com.carissa.revibes.point"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core"))
}
