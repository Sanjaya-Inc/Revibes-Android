pluginManagement {
    includeBuild("sjy-build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("sjy") {
            from(files("sjy-build-logic/gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "Revibes"
include(":app", ":features")
include(":core")
include(":features:auth")
include(":features:onboarding")
include(":features:home")
include(":features:profile")
include(":features:transaction-history")
include(":features:shop")
include(":features:exchange-points")
include(":features:drop-off")
include(":features:help-center")
include(":features:admin-menu")
include(":features:pick-up")
include(":features:point")
include(":features:home-admin")
include(":features:manage-users")
include(":features:manage-voucher")
include(":features:manage-transaction")
include(":features:manage-claimed-vouchers")
