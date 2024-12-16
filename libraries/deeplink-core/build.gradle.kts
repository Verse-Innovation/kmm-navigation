plugins {
    id("io.verse.kmm.library")
}

apply("${project.rootProject.file("gradle/github_repo_access.gradle")}")

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.tagd.arch)
                api(libs.verse.soa)
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.androidx.appcompat)
                api(libs.tagd.android)
                api(libs.facebook.android.sdk)
                api(libs.facebook.applinks)
                api(platform("com.google.firebase:firebase-bom:31.5.0"))
                api("com.google.firebase:firebase-dynamic-links-ktx")
                api("com.google.firebase:firebase-analytics-ktx")
            }
        }
    }
}

android {
    namespace = "io.verse.deeplink.core"
}

pomBuilder {
    description.set("deeplink core library")
}