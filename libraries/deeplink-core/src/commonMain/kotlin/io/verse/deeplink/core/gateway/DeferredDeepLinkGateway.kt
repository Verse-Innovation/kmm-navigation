package io.verse.deeplink.core.gateway

import io.tagd.langx.Callback
import io.tagd.langx.Intent
import io.verse.architectures.soa.gateway.DefaultSubscribablePullServiceGateway
import io.verse.architectures.soa.io.PullServiceResponse
import io.verse.deeplink.core.DeepLinkServicePullGateway
import io.verse.deeplink.core.DeferredDeepLinkServiceSpec
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.model.DeepLinkException

abstract class DeferredDeepLinkGateway :
    DefaultSubscribablePullServiceGateway<Intent, PullServiceResponse<DeepLink>, DeepLink>(),
    DeepLinkServicePullGateway {

    @Suppress("UNCHECKED_CAST")
    protected open fun <T : DeferredDeepLinkServiceSpec> service(): T? {
        return service as? T
    }

    override fun fetch(
        request: Intent,
        success: Callback<List<DeepLink>>?,
        failure: Callback<Throwable>?
    ) {

        throw UnsupportedOperationException("use fetchIfAny")
    }

    abstract fun fetchIfAny(
        intent: Intent,
        success: Callback<DeepLink>? = null,
        upgrade: Callback<Intent>? = null,
        failure: Callback<DeepLinkException>? = null
    )

    abstract fun resolve(
        url: String,
        success: Callback<DeepLink>?,
        upgrade: Callback<Intent>?,
        failure: Callback<DeepLinkException>?
    )

    protected fun resolveAppLink(
        link: String,
        rank: Int = 0,
        success: Callback<DeepLink>? = null
    ) {

        success?.invoke(service<DeferredDeepLinkServiceSpec>()!!.toDeepLink(link, rank))
    }

}
