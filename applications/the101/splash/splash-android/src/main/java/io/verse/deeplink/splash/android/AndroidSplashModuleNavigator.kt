package io.verse.deeplink.splash.android

import io.tagd.arch.access.library
import io.tagd.langx.Callback
import io.tagd.langx.Context
import io.tagd.langx.Intent
import io.tagd.langx.ref.WeakReference
import io.verse.deeplink.core.DeepLinking
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.model.DeepLinkConfig
import io.verse.deeplink.splash.android.impl.presentation.SplashActivity
import io.verse.deeplink.splash.splash.AbstractSplashModuleNavigator
import io.verse.the101.navigation.registry.MyAppDestinationRegistry.SplashServices

class AndroidSplashModuleNavigator(
    weakContext: WeakReference<Context>,
    handle: String
) : AbstractSplashModuleNavigator(
    weakContext = weakContext,
    handle = handle,
) {

    override fun registerDestinations(outPaths: ArrayList<String>) {
        registry?.register(
            destinationOf(
                pathAsAction = outPaths.include(SplashServices.PATH_TO_SPLASH),
                SplashActivity::class
            )
        )
    }

    override fun handle(
        dataObject: DeepLink,
        result: Callback<List<Intent>>?,
        error: Callback<Throwable>?
    ) {

        val config = library<DeepLinking>()?.config!!

        val defaultDeepLink = DeepLink(
            url = config.defaultLink(dataObject.url),
            minSupportedVersion = 1
        )

        super.handle(defaultDeepLink, result, error)
    }

    private fun DeepLinkConfig.defaultLink(unhandledUrl: String): String {
        return linkTo(
            "/$handle",
            SplashServices.PATH_TO_SPLASH
        ) + "?unhandled=${unhandledUrl}"
    }
}