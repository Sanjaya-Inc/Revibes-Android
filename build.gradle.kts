// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(core.plugins.android.application) apply false
    alias(core.plugins.kotlin.android) apply false
    alias(core.plugins.kotlin.compose) apply false
    alias(core.plugins.ksp) apply false
    alias(core.plugins.detekt) apply true
    id("com.sanjaya.buildlogic.detekt") apply true
}
