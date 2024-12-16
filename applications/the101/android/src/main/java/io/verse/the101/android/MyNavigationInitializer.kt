package io.verse.the101.android

import io.tagd.arch.access.library
import io.tagd.arch.scopable.library.Library
import io.tagd.di.bindLazy
import io.tagd.langx.Callback
import io.verse.deeplink.core.DeepLinking
import io.verse.deeplink.core.model.DeepLinkConfig
import io.verse.navigation.core.Navigation
import io.verse.navigation.core.NavigationInitializer
import io.verse.the101.navigation.registry.MyAppDestinationRegistry

class MyNavigationInitializer(app: MyApplication) : NavigationInitializer<MyApplication>(app) {

    override fun initialize(callback: Callback<Unit>) {
        outerScope.bindLazy<Library, Navigation> {
            val deepLinking = outerScope.library<DeepLinking>()!!

            new(
                newDependencies() + arrayOf(
                    ARG_DESTINATION_REGISTRY to MyAppDestinationRegistry(deepLinkConfig()),
                    ARG_DEEPLINKING to deepLinking,
                )
            )
        }

        super.initialize(callback)
    }

    private fun deepLinkConfig(): DeepLinkConfig {
        return DeepLinkConfig(
            defaultAppScheme = BuildConfig.APP_SCHEME,
            defaultAppHost = BuildConfig.APP_HOST,
            dynamicLinkDomains = listOf(BuildConfig.FIREBASE_DYNAMIC_LINK_DOMAIN),
            appLinkDomains = listOf(BuildConfig.DEEP_LINK_DOMAIN),
            schemes = listOf(BuildConfig.APP_SCHEME)
        )
    }
}