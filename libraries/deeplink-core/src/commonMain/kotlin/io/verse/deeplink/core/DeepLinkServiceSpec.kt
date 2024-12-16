package io.verse.deeplink.core

import io.tagd.langx.Callback
import io.tagd.langx.Intent
import io.verse.architectures.soa.provider.SubscriptionProfile
import io.verse.architectures.soa.service.DefaultSubscribablePushService
import io.verse.deeplink.core.handler.DeepLinkDispatcher
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.model.DeepLinkConfig

open class DeepLinkServiceSpec(
    name: String = "deep-linking/deep-link",
    override val provider: DeepLinkServiceProvider,
    subscriptionProfile: SubscriptionProfile? = null,
    open val config: DeepLinkConfig,
    override val dispatcher: DeepLinkDispatcher?
) : DefaultSubscribablePushService<DeepLink, List<Intent>>(
    name = name,
    provider = provider,
    subscriptionProfile = subscriptionProfile,
    dispatcher = dispatcher
) {

    fun containsAppDomain(host: String): Boolean {
        return config.containsAppDomain(host)
    }

    fun toDeepLink(appLink: String, rank: Int): DeepLink {
        return config.toDeepLink(appLink, rank)
    }

    fun dispatch(
        dataObject: DeepLink,
        result: ((List<Intent>) -> Unit)?,
        error: ((Throwable) -> Unit)?
    ) {

        dispatcher?.dispatch(dataObject, result, error)
    }

    fun canDispatch(dataObject: DeepLink, result: Callback<Boolean>) {
        dispatcher?.canDispatch(dataObject, result)
    }

    override fun release() {
        gateway = null
    }

}