package io.verse.deeplink.core

import io.tagd.langx.Intent
import io.verse.architectures.soa.gateway.SubscribablePullServiceGateway
import io.verse.architectures.soa.io.PullServiceResponse
import io.verse.architectures.soa.provider.ServiceProviderPartner
import io.verse.deeplink.core.model.DeepLink

interface DeferredDeepLinkServiceProvider : DeepLinkServiceProvider {

    companion object {
        const val GENRE = "deep-linking/deferred-deep-link"
    }

}

interface DeferredDeepLinkServiceProviderPartner : DeferredDeepLinkServiceProvider,
    ServiceProviderPartner

interface DeepLinkServicePullGateway : DeepLinkServiceGateway,
    SubscribablePullServiceGateway<Intent, PullServiceResponse<DeepLink>, DeepLink>