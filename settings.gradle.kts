pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "navigation"
include(":applications:the101:android")
include(":applications:the101:m1")
include(":applications:the101:m2")
include(":applications:the101:navigation-registry")

include(":libraries:deeplink-core")
include(":libraries:deeplink-android")
include(":libraries:navigation-core")
include(":libraries:navigation-android")
include(":applications:the101:m3")
include(":applications:the101:splash")
include(":applications:the101:splash:splash-android")
include(":applications:the101:splash:splash-core")
