@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.deeplink.core.sp.facebook

import io.verse.architectures.soa.provider.DefaultServiceProvider
import io.verse.architectures.soa.provider.SubscriptionProfile
import io.verse.deeplink.core.DeferredDeepLinkServiceProvider
import io.verse.deeplink.core.DeferredDeepLinkServiceSpec
import io.verse.deeplink.core.model.DeepLinkConfig

expect class FacebookDynamicLinkServiceProvider(
    genre: String
) : DefaultServiceProvider, DeferredDeepLinkServiceProvider

open class FacebookDynamicLinkService(
    override val provider: FacebookDynamicLinkServiceProvider,
    subscriptionProfile: SubscriptionProfile?,
    config: DeepLinkConfig
) : DeferredDeepLinkServiceSpec(
    name = "deep-linking/deferred-deep-link/facebook/dynamic-link",
    provider = provider,
    subscriptionProfile = subscriptionProfile,
    config = config
)