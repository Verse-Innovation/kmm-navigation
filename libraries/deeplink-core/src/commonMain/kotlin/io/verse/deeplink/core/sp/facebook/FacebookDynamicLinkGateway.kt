@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.deeplink.core.sp.facebook

import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.deeplink.core.gateway.DeferredDeepLinkProviderGateway

expect open class FacebookDynamicLinkGateway(
    weakContext: WeakReference<Context>,
    rank: Int
) : DeferredDeepLinkProviderGateway