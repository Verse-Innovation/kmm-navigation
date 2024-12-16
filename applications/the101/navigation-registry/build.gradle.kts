plugins {
    id("io.verse.kmm.library")
}

apply("${project.rootProject.file("gradle/github_repo_access.gradle")}")

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":libraries:navigation-core"))
            }
        }
    }
}

android {
    namespace = "io.verse.the101.navigation.registry"
}

pomBuilder {
    description.set("app specific navigation registry module")
}