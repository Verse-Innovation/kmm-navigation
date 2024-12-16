@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.navigation.core

import io.tagd.arch.scopable.AbstractWithinScopableInitializer
import io.tagd.arch.scopable.Scopable

expect open class NavigationInitializer<S : Scopable>(within: S) :
    AbstractWithinScopableInitializer<S, Navigation> {

    companion object {

        val ARG_DESTINATION_REGISTRY: String

        val ARG_DEEPLINKING: String

        val ARG_OUTER_SCOPE: String
    }
}