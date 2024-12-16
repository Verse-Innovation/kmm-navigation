package io.verse.deeplink.core

import io.tagd.langx.Callback
import io.tagd.langx.Intent
import io.verse.architectures.soa.provider.SubscriptionProfile
import io.verse.architectures.soa.service.DefaultSubscribablePullService
import io.verse.deeplink.core.gateway.DeferredDeepLinkGatewayAggregator
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.model.DeepLinkConfig
import io.verse.deeplink.core.model.DeepLinkException

open class DeferredDeepLinkServiceSpec(
    name: String = "deep-linking/deferred-deep-link",
    override val provider: DeepLinkServiceProvider,
    subscriptionProfile: SubscriptionProfile? = null,
    open val config: DeepLinkConfig
) : DefaultSubscribablePullService<Intent, DeepLink>(
    name = name,
    provider = provider,
    subscriptionProfile = subscriptionProfile
) {

    fun containsDynamicHost(host: String): Boolean {
        return config.containsDynamicHost(host)
    }

    fun containsAppScheme(host: String): Boolean {
        return config.containsAppScheme(host)
    }

    fun containsAppDomain(host: String): Boolean {
        return config.containsAppDomain(host)
    }

    fun currentAppVersion(): Int {
        return config.currentAppVersionProvider?.value() ?: 1
    }

    fun toDeepLink(appLink: String, rank: Int): DeepLink {
        return config.toDeepLink(appLink, rank)
    }

    fun fetchIfAny(
        intent: Intent,
        success: Callback<DeepLink>? = null,
        upgrade: Callback<Intent>? = null,
        failure: Callback<DeepLinkException>? = null
    ) {

        gateway<DeferredDeepLinkGatewayAggregator>()?.fetchIfAny(intent, success, upgrade, failure)
    }

    fun resolve(
        url: String,
        success: Callback<DeepLink>?,
        upgrade: Callback<Intent>?,
        failure: Callback<DeepLinkException>?
    ) {

        gateway<DeferredDeepLinkGatewayAggregator>()?.resolve(url, success, upgrade, failure)
    }

    override fun release() {
        gateway = null
    }

}

