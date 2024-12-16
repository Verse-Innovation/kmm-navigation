@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.navigation.core

import io.tagd.core.Service
import io.tagd.langx.Intent
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.model.DeepLinkConfig

actual open class DestinationRegistry actual constructor(config: DeepLinkConfig) :
    Service {

    actual fun register(vararg destinations: Destination) {
    }

    actual fun destination(path: String): Destination? {
        TODO("Not yet implemented")
    }

    actual fun deepLink(
        path: String,
        vararg args: Pair<String, Any?>
    ): DeepLink? {
        TODO("Not yet implemented")
    }

    actual fun deepLink(
        destination: Destination,
        vararg args: Pair<String, Any?>
    ): DeepLink {
        TODO("Not yet implemented")
    }

    actual fun intent(
        destination: Destination,
        vararg args: Pair<String, Any?>
    ): Intent {
        TODO("Not yet implemented")
    }

    actual override fun release() {
        TODO("Not yet implemented")
    }
}