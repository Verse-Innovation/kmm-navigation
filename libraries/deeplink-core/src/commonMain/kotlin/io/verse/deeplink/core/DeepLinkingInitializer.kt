@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.deeplink.core

import io.tagd.arch.scopable.AbstractWithinScopableInitializer
import io.tagd.arch.scopable.Scopable
import io.tagd.langx.Context

expect open class DeepLinkingInitializer<S : Scopable>(context: Context, within: S) :
    AbstractWithinScopableInitializer<S, DeepLinking> {

    companion object {
        val ARG_APPLICATION_SERVICE_CONSUMER: String

        val ARG_DEEP_LINK_CONFIG: String

        val ARG_DEFAULT_HANDLER_NAME: String

        val ARG_DISPATCHER: String
    }
}