package io.verse.deeplink.core.handler

import io.tagd.langx.Intent
import io.verse.architectures.soa.dispatcher.DefaultServiceDataObjectDispatcher
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandlerFactory
import io.verse.deeplink.core.model.DeepLink

open class DeepLinkDispatcher(
    override val factory: ServiceDataObjectHandlerFactory<DeepLink, List<Intent>>,
    val defaultHandlerName: String = ServiceDataObjectHandler.DEFAULT_HANDLE
) : DefaultServiceDataObjectDispatcher<DeepLink, List<Intent>>(factory) {

    override fun defaultHandler(): ServiceDataObjectHandler<DeepLink, List<Intent>>? {
        return handler(defaultHandlerName)
    }
}



