apply(plugin = "com.android.application")
apply(plugin = "kotlin-android")
apply(plugin = "kotlin-android-extensions")

typealias BuildConfigBuidler = ((String) -> LinkedHashMap)
typealias ResourceStringBuidler = ((LinkedHashMap) -> LinkedHashMap)
typealias ManifestPlaceHolderBuidler = ((LinkedHashMap) -> LinkedHashMap)
typealias VariantConfigurer = (
        (
        String,
        ApplicationVariant,
        BuildConfigBuidler? = null,
        ResourceStringBuidler? = null,
ManifestPlaceHolderBuidler? = null
) -> Unit
)

extra.apply {
    val variantConfigurer: VariantConfigurer? = configVariant
    set("appName", "myFancyAppName")
    set("commitId", "1-todo")
    set("variantConfigurer", variantConfigurer)
}

android {
    buildFeatures {
        buildConfig = true
        resValues = true
    }
    signingConfigs {
        getByName("debug") {
            storeFile = file("$rootDir/keystore/debug.keystore")
        }
        create("upload") {
            storePassword = System.getenv("UPLOAD_KEY_STORE_PASSWORD")
            keyAlias = System.getenv("UPLOAD_KEY_ALIAS")
            keyPassword = System.getenv("UPLOAD_KEY_PASSWORD")
            storeFile = file("$rootDir/.signing/upload.keystore")
        }
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            multiDexEnabled = true
            isShrinkResources = false
            matchingFallbacks.add("develop")
            applicationIdSuffix = ".develop"
        }
        getByName("release") {
            isMinifyEnabled = true
            multiDexEnabled = true
            isShrinkResources = true
            matchingFallbacks.add("release")
            lint.abortOnError = false
        }
        create("nightly") {
            // a fresh feature build, which might be buggy, the targets are devs, QAs
            // and stakeholders
            initWith(getByName("debug"))

            isDebuggable = true
            applicationIdSuffix = ".develop.nightly"
            versionNameSuffix = "-nightly-${extra["commitId"]}"
        }
        create("staging") {
            // a closed beta users, hence a release build with debugging on
            initWith(getByName("release"))

            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging-${extra["commitId"]}"
        }
        create("alpha") {
            // a public alpha users, typically published at firebase distribution or in house
            initWith(getByName("release"))

            applicationIdSuffix = ".release.alpha"
            versionNameSuffix = ".alpha-${extra["commitId"]}"
        }
        create("beta") {
            // a public alpha users, typically published at firebase distribution or in house
            initWith(getByName("release"))

            applicationIdSuffix = ".release.beta"
            versionNameSuffix = ".beta-${extra["commitId"]}"
        }
    }
    applicationVariants.all { variant ->
        @Suppress("UNCHECKED_CAST")
        val variantConfigurer = extra["variantConfigurer"] as? VariantConfigurer
        variantConfigurer?.invoke(extra["appName"] as String, variant)

        true
    }
}

fun configVariant(
    appName: String,
    variant: com.android.build.gradle.api.ApplicationVariant?,
    buildConfigBuilder: BuildConfigBuidler? = null,
    resourceStringBuilder: ResourceStringBuidler? = null,
    manifestPlaceHolderBuilder: ManifestPlaceHolderBuidler? = null,
) {

    val variantBuildConfig = allVariantsBuildConfig(appName)
    println("Setting configurations for $appName:${variant.buildType.name}")
}

fun allVariantsBuildConfig(appName: String) {
    val config = linkedMapOf<String, VariantValue?>()

}
