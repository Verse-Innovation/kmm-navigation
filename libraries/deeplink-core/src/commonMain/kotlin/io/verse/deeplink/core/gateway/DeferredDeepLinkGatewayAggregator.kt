package io.verse.deeplink.core.gateway

import io.tagd.arch.data.gateway.Gateway
import io.tagd.langx.Callback
import io.tagd.langx.Intent
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.model.DeepLinkException
import kotlin.reflect.KClass

open class DeferredDeepLinkGatewayAggregator : DeferredDeepLinkGateway() {

    private var delegates = linkedSetOf<DeferredDeepLinkProviderGateway>()

    fun add(delegate: DeferredDeepLinkProviderGateway) {
        delegates.add(delegate)
    }

    override fun fetchIfAny(
        intent: Intent,
        success: Callback<DeepLink>?,
        upgrade: Callback<Intent>?,
        failure: Callback<DeepLinkException>?
    ) {

        //case 1 -- if all failed
        //case 2 -- if any having success
        //case 3 -- if any having upgrade
        //case 4 -- 1 or more success -- invalid case?
        //case 5 -- 1 ore more upgrade -- invalid case?

        val zipper = ResponseZipper<KClass<out Gateway>, DeepLink, Intent>(
            size = delegates.size,
            eagerlyExecuteDoneOnSuccess = true
        ) { results ->

            if (results.successes.isNotEmpty()) {
                val rankedSuccesses = results.successes.values.toList().sorted()
                val highestRanked = rankedSuccesses[0]
                success?.invoke(highestRanked)
            } else if (results.upgrades.isNotEmpty()) {
                val rankedUpgrades = results.upgrades.values.toList()
                val highestRanked = rankedUpgrades[0]
                upgrade?.invoke(highestRanked)
            } else if (results.failures.isNotEmpty()) {
                val rankedFailures = results.failures.values.toList()
                val highestRanked = rankedFailures[0]
                failure?.invoke(
                    DeepLinkException( //todo append all errors
                        message = "# of failures ${results.failures.size}",
                        cause = highestRanked
                    )
                )
            }
        }

        delegates.toList().sortedBy { it.rank }.forEach { gateway ->
            gateway.fetchIfAny(intent, success = {
                zipper.success(gateway::class, it)
            }, upgrade = {
                zipper.upgrade(gateway::class, it)
            }, failure = {
                zipper.failure(gateway::class, it)
            })
        }
    }

    override fun resolve(
        url: String,
        success: Callback<DeepLink>?,
        upgrade: Callback<Intent>?,
        failure: Callback<DeepLinkException>?
    ) {

        var foundResolver = false
        for (delegate in delegates) {
            if (delegate.isUrlPatternMatches(url)) {
                foundResolver = true
                delegate.resolve(url, success, upgrade, failure)
                break
            }
        }
        if (!foundResolver) {
            resolveAppLink(link = url, success = success)
        }
    }

    override fun release() {
        delegates.clear()
        super.release()
    }
}
