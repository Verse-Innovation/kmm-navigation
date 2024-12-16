package io.verse.the101.android

import io.tagd.arch.access.library
import io.tagd.arch.scopable.library.Library
import io.tagd.core.Dependencies
import io.tagd.di.bindLazy
import io.tagd.langx.Callback
import io.verse.architectures.soa.Soa
import io.verse.deeplink.core.DeepLinking
import io.verse.deeplink.core.DeepLinkingInitializer
import io.verse.deeplink.core.model.DeepLinkConfig

class MyDeepLinkingInitializer(app: MyApplication) :
    DeepLinkingInitializer<MyApplication>(app, app) {

    override fun initialize(callback: Callback<Unit>) {
        outerScope.bindLazy<Library, DeepLinking> {
            val soa = outerScope.library<Soa>()!!

            new(dependencies = Dependencies().also {
                it.put(ARG_DEEP_LINK_CONFIG, deepLinkConfig())
                it.put(ARG_APPLICATION_SERVICE_CONSUMER, soa.consumer)
                it.put(ARG_OUTER_SCOPE, outerScope)
            })
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