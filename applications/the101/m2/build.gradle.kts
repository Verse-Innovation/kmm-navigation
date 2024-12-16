plugins {
    kotlin("multiplatform")
    id("io.verse.kmm.library")
}

apply("${project.rootProject.file("gradle/github_repo_access.gradle")}")

kotlin {
    sourceSets {
        val commonMain by getting  {
            dependencies {
                api(project(":applications:the101:navigation-registry"))
                implementation(libs.androidx.constraintlayout)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(project(":libraries:deeplink-android"))
                implementation(project(":libraries:navigation-android"))
            }
        }
    }
}

android {
    namespace = "io.verse.deeplink.android.m2"

    buildFeatures {
        viewBinding = true
    }
}

pomBuilder {
    description.set("the 101 app's m2 module")
}