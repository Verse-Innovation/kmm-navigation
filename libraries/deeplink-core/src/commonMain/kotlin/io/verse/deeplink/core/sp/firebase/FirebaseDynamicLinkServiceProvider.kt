@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.deeplink.core.sp.firebase

import io.verse.architectures.soa.provider.DefaultServiceProvider
import io.verse.architectures.soa.provider.SubscriptionProfile
import io.verse.deeplink.core.DeferredDeepLinkServiceProvider
import io.verse.deeplink.core.DeferredDeepLinkServiceSpec
import io.verse.deeplink.core.model.DeepLinkConfig

expect class FirebaseDynamicLinkServiceProvider(
    genre: String
) : DefaultServiceProvider, DeferredDeepLinkServiceProvider

open class FirebaseDynamicLinkService(
    override val provider: FirebaseDynamicLinkServiceProvider,
    subscriptionProfile: SubscriptionProfile?,
    config: DeepLinkConfig
) : DeferredDeepLinkServiceSpec(
    name = "deep-linking/deferred-deep-link/firebase/dynamic-link",
    provider = provider,
    subscriptionProfile = subscriptionProfile,
    config = config
)