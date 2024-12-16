package io.verse.deeplink.core

import io.tagd.arch.access.library
import io.tagd.arch.scopable.library.Library
import io.tagd.di.Scope
import io.tagd.di.bind
import io.tagd.langx.Callback
import io.tagd.langx.Intent
import io.verse.architectures.soa.Soa
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandlerFactory
import io.verse.architectures.soa.provider.ServiceProviderLibrary
import io.verse.deeplink.core.handler.DeepLinkHandler
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.model.DeepLinkConfig
import io.verse.deeplink.core.model.DeepLinkException
import io.verse.deeplink.core.sp.inhouse.DefaultDeepLinkServiceProvider

@Suppress("unused")
class DeepLinking private constructor(name: String, outerScope: Scope) :
    ServiceProviderLibrary<DeepLinkServiceProvider>(name = name, outerScope = outerScope) {

    lateinit var config: DeepLinkConfig
        private set

    @Suppress("MemberVisibilityCanBePrivate")
    val deepLinkServiceSpec: DeepLinkServiceSpec?
        get() = (get(DefaultDeepLinkServiceProvider::class))?.service(DeepLinkServiceSpec::class)

    val deferredDeepLinkServiceSpec: DeferredDeepLinkServiceAggregator?
        get() = (get(DeferredDeepLinkServiceProviderAggregator::class))
            ?.aggregatorService

    val dispatcher
        get() = deepLinkServiceSpec?.dispatcher

    fun fetchIfAny(
        intent: Intent,
        success: Callback<DeepLink>? = null,
        upgrade: Callback<Intent>? = null,
        failure: Callback<DeepLinkException>? = null
    ) {

        deferredDeepLinkServiceSpec?.fetchIfAny(intent, success, upgrade, failure)
    }

    fun resolve(
        url: String,
        success: Callback<DeepLink>?,
        upgrade: Callback<Intent>?,
        failure: Callback<DeepLinkException>?
    ) {

        deferredDeepLinkServiceSpec?.resolve(url, success, upgrade, failure)
    }

    fun dispatch(
        dataObject: DeepLink,
        result: Callback<List<Intent>>? = null,
        error: Callback<Throwable>? = null
    ) {

        deepLinkServiceSpec?.dispatch(dataObject, result, error)
    }

    fun canDispatch(
        dataObject: DeepLink,
        result: Callback<Boolean>
    ) {

        deepLinkServiceSpec?.canDispatch(dataObject, result)
    }

    private fun getDeeplinkHandlerFactory()
        : ServiceDataObjectHandlerFactory<DeepLink, List<Intent>>? {

        return deepLinkServiceSpec?.dispatcher?.factory
    }

    fun getDeeplinkHandler(handle: String): ServiceDataObjectHandler<DeepLink, List<Intent>>? {
        return getDeeplinkHandlerFactory()?.get(handle)
    }

    fun putDeeplinkHandler(deepLinkHandler: DeepLinkHandler) {
        getDeeplinkHandlerFactory()?.put(deepLinkHandler.handle, deepLinkHandler)
    }

    class Builder : ServiceProviderLibrary.Builder<DeepLinkServiceProvider, DeepLinking>() {

        private lateinit var config: DeepLinkConfig

        override fun name(name: String?): Builder {
            this.name = name
            return this
        }

        override fun scope(outer: Scope?): Builder {
            super.scope(outer)
            return this
        }

        override fun inject(bindings: Scope.(DeepLinking) -> Unit): Builder {
            super.inject(bindings)
            return this
        }

        override fun consumer(consumer: ApplicationServiceConsumer, genre: String): Builder {
            super.consumer(consumer, genre)
            return this
        }

        fun config(config: DeepLinkConfig): Builder {
            this.config = config
            return this
        }

        override fun buildLibrary(): DeepLinking {
            return DeepLinking(
                name = name ?: "${outerScope.name}/$NAME",
                outerScope = outerScope
            ).also { library ->

                library.config = config
                library.factory = factory

                outerScope.bind<Library, DeepLinking>(instance = library) // as 3rd party lib offering
                outerScope.library<Soa>()?.put(library) // as soa lib offering
            }
        }

        companion object {
            const val NAME = "deeplinking"
        }
    }
}