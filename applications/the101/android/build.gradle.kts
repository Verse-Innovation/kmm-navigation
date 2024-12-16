plugins {
    id("io.verse.android.application")
    id("com.google.gms.google-services")
}

apply("${project.rootProject.file("gradle/github_repo_access.gradle")}")

android {
    namespace = "io.verse.the101.android"

    defaultConfig {
        applicationId = "io.verse.deeplink.android"

        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        configDeepLinking()
    }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
}

dependencies {
    implementation(project(":applications:the101:m1"))
    implementation(project(":applications:the101:m2"))
    implementation(project(":applications:the101:m3"))
    implementation(project(":applications:the101:splash:splash-android"))
    implementation(project(":applications:the101:splash:splash-core"))
    implementation(project(":applications:the101:m3"))
    implementation(project(":applications:the101:navigation-registry"))
    implementation(project(":libraries:deeplink-android"))
    implementation(project(":libraries:navigation-android"))

    implementation("androidx.compose.ui:ui:1.4.3")
    implementation("androidx.compose.ui:ui-tooling:1.4.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
    implementation("androidx.compose.foundation:foundation:1.4.3")
    implementation("androidx.compose.material:material:1.4.3")
    implementation("androidx.activity:activity-compose:1.7.2")
}

flavorVariantFieldConfig {
    buildConfigFieldBuilder.set { flavor, variants -> //todo move to other kts and refer here
        linkedMapOf()
    }
    resFieldBuilder.set { flavor, variants -> //todo move to other kts and refer here
        linkedMapOf()
    }
    manifestPlaceholderBuilder.set { flavor, variants -> //todo move to other kts and refer here
        linkedMapOf()
    }
}

fun com.android.build.api.dsl.ApplicationDefaultConfig.configDeepLinking() {
    val appScheme = "deeplink"
    val appHost = "sample"
    val firebaseDynamicLinkDomain = "apps.publicvibe.com"
    val deepLinkDomain = "www.publicvibe.com"

    manifestPlaceholders["appScheme"] = appScheme
    manifestPlaceholders["appHost"] = appHost
    manifestPlaceholders["firebaseDynamicLinkDomain"] = firebaseDynamicLinkDomain
    manifestPlaceholders["deepLinkDomain"] = deepLinkDomain

    buildConfigField("APP_SCHEME", "$appScheme://")
    buildConfigField("APP_HOST", appHost)
    buildConfigField("FIREBASE_DYNAMIC_LINK_DOMAIN", firebaseDynamicLinkDomain)
    buildConfigField("DEEP_LINK_DOMAIN", deepLinkDomain)
}

inline fun <reified ValueT> com.android.build.api.dsl.VariantDimension.buildConfigField(
    name: String,
    value: ValueT
) {
    val resolvedValue = when (value) {
        is String -> "\"$value\""
        else -> value
    }.toString()
    buildConfigField(ValueT::class.java.simpleName, name, resolvedValue)
}
