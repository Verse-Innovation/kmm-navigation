@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.navigation.core

import io.tagd.arch.present.mvnp.Navigatable
import io.tagd.arch.present.mvnp.Navigator
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.deeplink.core.handler.DefaultDeepLinkHandler

actual abstract class AbstractDeepLinkNavigator<N : Navigatable> actual constructor(
    private val weakContext: WeakReference<Context>,
    handle: String
) : DefaultDeepLinkHandler(handle), Navigator<N> {

    protected actual var registry: DestinationRegistry? = null
    actual override var navigatable: N? = null

    actual override fun release() {
    }
}