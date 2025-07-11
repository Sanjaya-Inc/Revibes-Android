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
        create("core") {
            from(files("sjy-version-catalog-core/core.versions.toml"))
        }
        create("ui") {
            from(files("sjy-version-catalog-ui/ui.versions.toml"))
        }
        create("essentials") {
            from(files("sjy-version-catalog-essentials/essentials.versions.toml"))
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
