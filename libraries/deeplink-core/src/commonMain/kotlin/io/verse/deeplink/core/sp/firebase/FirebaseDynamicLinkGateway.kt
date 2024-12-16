@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.deeplink.core.sp.firebase

import io.verse.deeplink.core.gateway.DeferredDeepLinkProviderGateway

expect open class FirebaseDynamicLinkGateway(rank: Int) : DeferredDeepLinkProviderGateway