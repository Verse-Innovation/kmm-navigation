package io.verse.deeplink.core.handler

import io.tagd.langx.Intent
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler
import io.verse.deeplink.core.model.DeepLink

interface DeepLinkHandler : ServiceDataObjectHandler<DeepLink, List<Intent>> {

    fun register(paths: List<String>, builder: DeepLinkIntentBuilder)

    fun register(builder: DeepLinkIntentBuilder, vararg paths: String)

    fun register(path: String, builder: DeepLinkIntentBuilder)
}
