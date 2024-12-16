@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.navigation.core

import androidx.core.os.bundleOf
import io.tagd.core.Service
import io.tagd.langx.Intent
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.model.DeepLinkConfig

actual open class DestinationRegistry actual constructor(private val config: DeepLinkConfig) :
    Service {

    private val destinations = hashMapOf<String, Destination>()

    actual fun register(vararg destinations: Destination) {
        destinations.forEach { destination ->
            this.destinations[destination.pathAsAction] = destination
        }
    }

    actual fun destination(path: String): Destination? {
        return destinations[path]
    }

    actual fun deepLink(
        path: String,
        vararg args: Pair<String, Any?>
    ): DeepLink? {

        return destinations[path]?.let { deepLink(it, *args) }
    }

    actual fun deepLink(
        destination: Destination,
        vararg args: Pair<String, Any?>
    ): DeepLink {

        val scheme = config.defaultAppScheme
        val host = config.defaultAppHost
        val authority = destination.authority
        val link = StringBuilder("$scheme$host/$authority${destination.pathAsAction}")

        args.forEachIndexed { index, entry ->
            if (index == 0) {
                link.append('?')
            } else {
                link.append('&')
            }
            link.append(entry.first).append('=').append(entry.second)
        }

        return DeepLink(link.toString(), 0)
    }

    actual fun intent(destination: Destination, vararg args: Pair<String, Any?>): Intent {
        return Intent(destination.pathAsAction).apply {
            putExtra(Destination.ARGUMENTS, bundleOf(*args))
            destination.remaining()?.let { remainingPath ->
                putExtra(Destination.REMAINING_PATH, remainingPath.pathAsAction)
                putExtra(
                    Destination.REMAINING_COMPONENTS,
                    arrayListOf<String>().apply { addAll(remainingPath.components) }
                )
            }
        }
    }

    actual override fun release() {
        destinations.clear()
    }
}