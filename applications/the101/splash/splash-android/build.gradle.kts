plugins {
    id("io.verse.android.library")
}
apply("${project.rootProject.file("gradle/github_repo_access.gradle")}")

android {
    namespace = "io.verse.deeplink.splash"

    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    api("io.github.pavan2you:kmm-tagd-android:0.0.0-alpha06-snapshot")
    implementation(project(":applications:the101:navigation-registry"))
    implementation(project(":applications:the101:splash:splash-core"))
    api(project(":libraries:deeplink-android"))
    api(project(":libraries:navigation-android"))

    implementation("androidx.compose.ui:ui:1.4.3")
    implementation("androidx.compose.ui:ui-tooling:1.4.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
    implementation("androidx.compose.foundation:foundation:1.4.3")
    implementation("androidx.compose.material:material:1.4.3")
    implementation("androidx.activity:activity-compose:1.7.2")
}

pomBuilder {
    description.set("splash android core module")
}