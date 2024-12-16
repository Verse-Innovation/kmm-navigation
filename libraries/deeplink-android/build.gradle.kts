plugins {
    id("io.verse.android.library")
}
apply("${project.rootProject.file("gradle/github_repo_access.gradle")}")

android {
    namespace = "io.verse.deeplink.android"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    api("io.github.pavan2you:kmm-tagd-android:0.0.0-alpha06-snapshot")
    api(project(":libraries:deeplink-core"))
    api(project(":libraries:navigation-android"))
}

pomBuilder {
    description.set("deep link android core library")
}