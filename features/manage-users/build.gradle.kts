plugins {
    alias(sjy.plugins.buildlogic.lib)
    alias(sjy.plugins.buildlogic.compose)
    alias(sjy.plugins.buildlogic.detekt)
}

android {
    namespace = "com.carissa.revibes.manage_users"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":features:exchange-points"))
    implementation(sjy.accompanist.permissions)
}
