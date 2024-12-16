package io.verse.deeplink.core.model

import io.tagd.arch.datatype.DataObject
import io.tagd.core.Expression
import io.tagd.core.ValueProvider
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler
import io.verse.deeplink.core.handler.DeepLinkDispatcher

data class DeepLinkConfig(
    val defaultAppScheme: String, // org://
    val defaultAppHost: String,
    val dynamicLinkDomains: List<String>,// [https://dynamic/link, https://myapp/dynamic/link]
    val appLinkDomains: List<String>, // [myapp-debug://app/link, myapp-staging://app/link]
    val schemes: List<String>, // [myapp-debug://, myapp-staging://]
    val authorizationChecker: Expression? = null,
    val currentAppVersionProvider: ValueProvider<Int>? = null,
    val dispatcher: DeepLinkDispatcher? = null,
    val defaultHandler: String? = ServiceDataObjectHandler.DEFAULT_HANDLE
) : DataObject() {

    fun containsDynamicHost(host: String): Boolean {
        return dynamicLinkDomains.contains(host)
    }

    fun containsAppScheme(scheme: String): Boolean {
        return schemes.contains(scheme)
    }

    fun containsAppDomain(host: String): Boolean {
        return appLinkDomains.contains(host)
    }

    fun toDeepLink(appLink: String, rank: Int): DeepLink {
        var deepLink = removeProtocols(appLink)
        appLinkDomains.forEach { appLinkDomain ->
            val temp = deepLink.replace(
                appLinkDomain,
                "$defaultAppScheme$defaultAppHost",
                true
            )
            if (temp != deepLink) {
                deepLink = temp
                return@forEach
            }
        }

        val minSupportedVersion = currentAppVersionProvider?.value() ?: 1
        return DeepLink(url = deepLink, minSupportedVersion = minSupportedVersion).also {
            it.rank = rank
        }
    }

    private fun removeProtocols(appLink: String): String {
        return appLink.removePrefix("https://").removePrefix("http://")
    }

    fun linkTo(authority: String, path: String): String {
        return defaultAppScheme + defaultAppHost + authority + path
    }
}
