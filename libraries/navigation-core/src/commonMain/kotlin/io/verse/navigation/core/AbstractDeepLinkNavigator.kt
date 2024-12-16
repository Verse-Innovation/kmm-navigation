@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.navigation.core

import io.tagd.arch.present.mvnp.Navigatable
import io.tagd.arch.present.mvnp.Navigator
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.deeplink.core.handler.DefaultDeepLinkHandler

expect abstract class AbstractDeepLinkNavigator<N : Navigatable>(
    weakContext: WeakReference<Context>,
    handle: String
) : DefaultDeepLinkHandler, Navigator<N> {

    protected var registry: DestinationRegistry?

    override var navigatable: N?

    override fun release()
}