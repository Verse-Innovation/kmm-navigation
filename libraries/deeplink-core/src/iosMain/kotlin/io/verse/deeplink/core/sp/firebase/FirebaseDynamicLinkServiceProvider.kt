@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.deeplink.core.sp.firebase

import io.verse.architectures.soa.provider.DefaultServiceProvider
import io.verse.deeplink.core.DeferredDeepLinkServiceProvider

actual class FirebaseDynamicLinkServiceProvider actual constructor(
    genre: String
) : DefaultServiceProvider(genre = genre),
    DeferredDeepLinkServiceProvider {

    companion object {
        const val GENRE = "deep-linking/deferred-deeplink/firebase"
    }
}