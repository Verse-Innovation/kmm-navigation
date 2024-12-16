@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.deeplink.core

import io.tagd.arch.access.library
import io.tagd.arch.domain.usecase.Command
import io.tagd.arch.scopable.AbstractWithinScopableInitializer
import io.tagd.arch.scopable.Scopable
import io.tagd.core.Dependencies
import io.tagd.di.key
import io.tagd.di.layer
import io.tagd.langx.Context
import io.verse.architectures.soa.Soa
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandlerFactory
import io.verse.architectures.soa.provider.SubscriptionProfile
import io.verse.deeplink.core.gateway.DeferredDeepLinkGatewayAggregator
import io.verse.deeplink.core.handler.DeepLinkDispatcher
import io.verse.deeplink.core.model.DeepLinkConfig
import io.verse.deeplink.core.sp.facebook.FacebookDynamicLinkGateway
import io.verse.deeplink.core.sp.facebook.FacebookDynamicLinkService
import io.verse.deeplink.core.sp.facebook.FacebookDynamicLinkServiceProvider
import io.verse.deeplink.core.sp.firebase.FirebaseDynamicLinkGateway
import io.verse.deeplink.core.sp.firebase.FirebaseDynamicLinkService
import io.verse.deeplink.core.sp.firebase.FirebaseDynamicLinkServiceProvider
import io.verse.deeplink.core.sp.inhouse.DefaultDeepLinkServiceProvider
import io.verse.deeplink.core.usecase.FetchDeferredDeepLinkIfAnyUsecase
import io.verse.deeplink.core.usecase.ResolveDeepLinkUsecase
import java.lang.ref.WeakReference

actual open class DeepLinkingInitializer<S : Scopable> actual constructor(
    context: Context, within: S
) : AbstractWithinScopableInitializer<S, DeepLinking>(within) {

    private var weakContext: WeakReference<Context>? = WeakReference(context)

    override fun new(dependencies: Dependencies): DeepLinking {
        assert(library<Soa>() != null)

        val consumer = dependencies.get<ApplicationServiceConsumer>(
            ARG_APPLICATION_SERVICE_CONSUMER
        )!!
        val config = dependencies.get<DeepLinkConfig>(ARG_DEEP_LINK_CONFIG)!!

        //Deferred Deep Link Provider
        val firebaseProvider = newFirebaseProvider(consumer, config)
        val facebookProvider = newFacebookProvider(consumer, config)
        val deferredDeepLinkProviderAggregator =
            DeferredDeepLinkServiceProviderAggregator().apply {
                this.consumer = consumer
                aggregatorService = DeferredDeepLinkServiceAggregator(this, config)
                aggregatorService?.aggregatorGateway = DeferredDeepLinkGatewayAggregator().also {
                    it.service = aggregatorService
                }
                addProvider(firebaseProvider)
                addProvider(facebookProvider)
            }

        //Plain Deep Link Provider
        val deepLinkProvider = DefaultDeepLinkServiceProvider().apply {
            this.consumer = consumer
            val dispatcher = config.dispatcher ?: DeepLinkDispatcher(
                defaultHandlerName = config.defaultHandler
                    ?: ServiceDataObjectHandler.DEFAULT_HANDLE,
                factory = ServiceDataObjectHandlerFactory()
            )
            val service = DeepLinkServiceSpec(
                provider = this,
                subscriptionProfile = null,
                config = config,
                dispatcher = dispatcher
            )
            putService(service)
        }

        return DeepLinking.Builder()
            .scope(outerScope)
            .config(config)
            .consumer(consumer, DeepLinkServiceProvider.GENRE)
            .inject {
                layer<Command<*, *>> {
                    bind(key(), ResolveDeepLinkUsecase())
                    bind(key(), FetchDeferredDeepLinkIfAnyUsecase())
                }
            }
            .build().also {
                consumer.putServiceProvider(
                    DeepLinkServiceProvider.GENRE,
                    deferredDeepLinkProviderAggregator
                )
                consumer.putServiceProvider(
                    DeepLinkServiceProvider.GENRE,
                    deepLinkProvider
                )
            }
    }

    private fun newFirebaseProvider(
        consumer: ApplicationServiceConsumer,
        config: DeepLinkConfig
    ): FirebaseDynamicLinkServiceProvider {

        return FirebaseDynamicLinkServiceProvider(
            genre = FirebaseDynamicLinkServiceProvider.GENRE
        ).apply {
            this.consumer = consumer
            putService(newFirebaseService(config, null))
        }
    }

    private fun FirebaseDynamicLinkServiceProvider.newFirebaseService(
        config: DeepLinkConfig,
        subscriptionProfile: SubscriptionProfile?
    ): FirebaseDynamicLinkService {

        return FirebaseDynamicLinkService(
            provider = this,
            subscriptionProfile = subscriptionProfile,
            config = config
        ).apply {
            newAssociateNewFirebaseGateway()
        }
    }

    private fun FirebaseDynamicLinkService.newAssociateNewFirebaseGateway() {
        val firebaseGateway = FirebaseDynamicLinkGateway(rank = 0)
        firebaseGateway.service = this
        gateway = firebaseGateway
    }

    private fun newFacebookProvider(
        consumer: ApplicationServiceConsumer,
        config: DeepLinkConfig
    ): FacebookDynamicLinkServiceProvider {

        return FacebookDynamicLinkServiceProvider(
            genre = FacebookDynamicLinkServiceProvider.GENRE
        ).apply {
            this.consumer = consumer
            putService(newFacebookService(config, null))
        }
    }

    private fun FacebookDynamicLinkServiceProvider.newFacebookService(
        config: DeepLinkConfig,
        subscriptionProfile: SubscriptionProfile?
    ): FacebookDynamicLinkService {

        return FacebookDynamicLinkService(
            provider = this,
            subscriptionProfile = subscriptionProfile,
            config = config
        ).apply {
            newAssociateNewFacebookGateway()
        }
    }

    private fun FacebookDynamicLinkService.newAssociateNewFacebookGateway() {
        val facebookGateway = FacebookDynamicLinkGateway(
            weakContext!!,
            rank = 1
        )
        facebookGateway.service = this
        gateway = facebookGateway
    }

    override fun release() {
        weakContext?.clear()
        super.release()
    }

    actual companion object {
        actual val ARG_APPLICATION_SERVICE_CONSUMER = "application_service_consumer"
        actual val ARG_DEEP_LINK_CONFIG = "deep_link_config"
        actual val ARG_DEFAULT_HANDLER_NAME = "default_handler_name"
        actual val ARG_DISPATCHER = "dispatcher"
    }
}