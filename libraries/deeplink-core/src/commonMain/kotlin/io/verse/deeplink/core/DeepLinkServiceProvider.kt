package io.verse.deeplink.core

import io.tagd.langx.Intent
import io.verse.architectures.soa.gateway.SubscribablePushServiceGateway
import io.verse.architectures.soa.gateway.SubscribableServiceGateway
import io.verse.architectures.soa.provider.ServiceProvider
import io.verse.architectures.soa.provider.ServiceProviderPartner
import io.verse.deeplink.core.model.DeepLink

interface DeepLinkServiceProvider : ServiceProvider {

    companion object {
        const val GENRE = "deep-linking"
    }

}

interface DeepLinkServiceProviderPartner : ServiceProviderPartner

interface DeepLinkServiceGateway : SubscribableServiceGateway

/**
 * Typically these are activities/services/components for which deeplinks are coming in
 */
interface DeepLinkServicePushGateway : DeepLinkServiceGateway,
    SubscribablePushServiceGateway<DeepLink, List<Intent>>