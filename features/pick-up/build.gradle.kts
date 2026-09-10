plugins {
    alias(sjy.plugins.buildlogic.lib)
    alias(sjy.plugins.buildlogic.compose)
    alias(sjy.plugins.buildlogic.detekt)
}

android {
    namespace = "com.carissa.revibes.pick_up"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core"))
}
