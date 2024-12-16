@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.navigation.core

import io.tagd.arch.scopable.AbstractWithinScopableInitializer
import io.tagd.arch.scopable.Scopable
import io.tagd.core.Dependencies

actual open class NavigationInitializer<S : Scopable> actual constructor(within: S) :
    AbstractWithinScopableInitializer<S, Navigation>(within) {

    override fun new(dependencies: Dependencies): Navigation {
        TODO("Not yet implemented")
    }

    actual companion object {
        actual val ARG_DESTINATION_REGISTRY: String
            get() = TODO("Not yet implemented")
        actual val ARG_DEEPLINKING: String
            get() = TODO("Not yet implemented")
        actual val ARG_OUTER_SCOPE: String
            get() = TODO("Not yet implemented")

    }
}