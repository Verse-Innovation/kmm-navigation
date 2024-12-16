@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.deeplink.core.sp.firebase

import io.tagd.langx.Callback
import io.tagd.langx.Intent
import io.verse.deeplink.core.gateway.DeferredDeepLinkProviderGateway
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.model.DeepLinkException

actual open class FirebaseDynamicLinkGateway actual constructor(rank: Int) :
    DeferredDeepLinkProviderGateway() {

    override fun fetchIfAny(
        intent: Intent,
        success: Callback<DeepLink>?,
        upgrade: Callback<Intent>?,
        failure: Callback<DeepLinkException>?
    ) {
        TODO("Not yet implemented")
    }

    override fun resolve(
        url: String,
        success: Callback<DeepLink>?,
        upgrade: Callback<Intent>?,
        failure: Callback<DeepLinkException>?
    ) {
        TODO("Not yet implemented")
    }

    override val rank: Int
        get() = TODO("Not yet implemented")

    override fun isUrlPatternMatches(url: String): Boolean {
        TODO("Not yet implemented")
    }
}