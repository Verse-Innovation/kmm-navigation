@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.deeplink.core

import io.tagd.arch.scopable.AbstractWithinScopableInitializer
import io.tagd.arch.scopable.Scopable
import io.tagd.core.Dependencies
import io.tagd.langx.Context

actual open class DeepLinkingInitializer<S : Scopable> actual constructor(
    context: Context, within: S
) : AbstractWithinScopableInitializer<S, DeepLinking>(within) {

    override fun new(dependencies: Dependencies): DeepLinking {
        TODO("Not yet implemented")
    }

    actual companion object {
        actual val ARG_APPLICATION_SERVICE_CONSUMER: String
            get() = TODO("Not yet implemented")
        actual val ARG_DEEP_LINK_CONFIG: String
            get() = TODO("Not yet implemented")
        actual val ARG_DEFAULT_HANDLER_NAME: String
            get() = TODO("Not yet implemented")
        actual val ARG_DISPATCHER: String
            get() = TODO("Not yet implemented")

    }
}