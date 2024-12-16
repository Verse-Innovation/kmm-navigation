package io.verse.deeplink.core

import io.tagd.langx.Intent
import io.tagd.langx.assert
import io.verse.architectures.soa.gateway.SubscribablePullServiceGateway
import io.verse.architectures.soa.io.PullServiceResponse
import io.verse.architectures.soa.provider.DefaultServiceProvider
import io.verse.architectures.soa.service.SubscribableService
import io.verse.deeplink.core.gateway.DeferredDeepLinkGatewayAggregator
import io.verse.deeplink.core.gateway.DeferredDeepLinkProviderGateway
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.model.DeepLinkConfig
import kotlin.reflect.KClass

/**
 * The aggregation must be the last step after fully prepared [DeferredDeepLinkServiceProvider] are
 * available. Calling before might cause unlinked aggregated gateway.
 */
class DeferredDeepLinkServiceProviderAggregator(
    genre: String = GENRE,
) : DefaultServiceProvider(genre = genre),
    DeferredDeepLinkServiceProvider {

    private val providers =
        hashMapOf<KClass<out DeferredDeepLinkServiceProvider>, DeferredDeepLinkServiceProvider>()

    var aggregatorService: DeferredDeepLinkServiceAggregator? = null
        set(value) {
            putService(value as SubscribableService)
            field = value
        }

    /**
     * Call this after all relevant service providers are fully prepared with services and
     * gateways.
     */
    fun addProvider(
        provider: DeferredDeepLinkServiceProvider,
        addGatewayToAggregator: Boolean = true
    ) {

        providers[provider::class] = provider
        if (addGatewayToAggregator) {
            provider.services.values.forEach { service ->
                service.gateway?.let {
                    aggregatorService?.addProviderGateway(it as DeferredDeepLinkProviderGateway)
                }
            }
        }
    }

    override fun release() {
        providers.values.forEach {
            it.release()
        }
        providers.clear()
        super.release()
    }

    companion object {
        const val GENRE = "deep-linking/deferred-deeplink/aggregator"
    }

}

open class DeferredDeepLinkServiceAggregator(
    override val provider: DeferredDeepLinkServiceProviderAggregator,
    config: DeepLinkConfig
) : DeferredDeepLinkServiceSpec(
    name = "$provider/service",
    provider = provider,
    subscriptionProfile = null,
    config = config
) {

    open var aggregatorGateway: DeferredDeepLinkGatewayAggregator? = null

    final override var gateway: SubscribablePullServiceGateway<
            Intent,
            PullServiceResponse<DeepLink>,
            DeepLink>? = null
        get() = aggregatorGateway
        set(value) {
            if (value !is DeferredDeepLinkGatewayAggregator) {
                throw IllegalArgumentException("value must be a DeferredDeepLinkCompositeGateway")
            }
            field = value
            aggregatorGateway = value
        }

    fun addProviderGateway(
        delegate: DeferredDeepLinkProviderGateway
    ): DeferredDeepLinkServiceAggregator {

        assert(aggregatorGateway != null)

        aggregatorGateway?.add(delegate)
        return this
    }

}