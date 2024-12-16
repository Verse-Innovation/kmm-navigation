package io.verse.deeplink.core.gateway

abstract class DeferredDeepLinkProviderGateway : DeferredDeepLinkGateway() {

    abstract val rank: Int

    abstract fun isUrlPatternMatches(url: String): Boolean
}