@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.navigation.core

import io.tagd.core.Service
import io.tagd.langx.Intent
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.model.DeepLinkConfig

expect open class DestinationRegistry(config: DeepLinkConfig) : Service {

    fun register(vararg destinations: Destination)

    fun destination(path: String): Destination?

    fun deepLink(path: String, vararg args: Pair<String, Any?>): DeepLink?

    fun deepLink(destination: Destination, vararg args: Pair<String, Any?>): DeepLink

    fun intent(destination: Destination, vararg args: Pair<String, Any?>): Intent

    override fun release()
}