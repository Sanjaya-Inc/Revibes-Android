plugins {
    alias(sjy.plugins.buildlogic.lib)
    alias(sjy.plugins.buildlogic.compose)
    alias(sjy.plugins.buildlogic.detekt)
    alias(sjy.plugins.buildlogic.test)
}

android {
    namespace = "com.carissa.revibes.drop_off"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core"))
    testImplementation(sjy.bundles.orbit.test)
}
