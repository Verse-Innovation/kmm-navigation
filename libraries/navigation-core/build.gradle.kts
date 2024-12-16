plugins {
    id("io.verse.kmm.library")
}

apply("${project.rootProject.file("gradle/github_repo_access.gradle")}")

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":libraries:deeplink-core"))
            }
        }
    }
}

android {
    namespace = "io.verse.navigation.core"
}

pomBuilder {
    description.set("navigation core library")
}