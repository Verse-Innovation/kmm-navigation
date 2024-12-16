package io.verse.the101.android

import io.tagd.android.app.TagdApplication
import io.tagd.arch.access.library
import io.tagd.langx.Callback
import io.tagd.langx.Intent
import io.verse.deeplink.core.DeepLinking
import io.verse.deeplink.core.handler.DefaultDeepLinkHandler
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.model.DeepLinkConfig
import io.verse.the101.navigation.registry.MyAppDestinationRegistry.SplashServices

class SplashDeepLinkHandler(application: TagdApplication) : DefaultDeepLinkHandler(
    handle = application.name
) {

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

    override fun release() {
        super.release()
    }
}