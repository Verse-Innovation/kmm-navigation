@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.navigation.core

import io.tagd.arch.scopable.AbstractWithinScopableInitializer
import io.tagd.arch.scopable.Scopable
import io.tagd.core.Dependencies
import io.tagd.di.Scope
import io.verse.deeplink.core.DeepLinking

actual open class NavigationInitializer<S : Scopable> actual constructor(within: S) :
    AbstractWithinScopableInitializer<S, Navigation>(within) {

    override fun new(dependencies: Dependencies): Navigation {
        val registry = dependencies.get<DestinationRegistry>(
            ARG_DESTINATION_REGISTRY
        )!!
        val deepLinking = dependencies.get<DeepLinking>(
            ARG_DEEPLINKING
        )!!
        val outerScope = dependencies.get<Scope>(
            ARG_OUTER_SCOPE
        )!!

        return Navigation.Builder()
            .scope(outerScope)
            .registry(registry)
            .deeplinking(deepLinking)
            .build()
    }

    actual companion object {
        actual val ARG_DESTINATION_REGISTRY: String = "destination_registry"
        actual val ARG_DEEPLINKING: String = "deeplinking"
        actual val ARG_OUTER_SCOPE: String = "outer_scope"
    }
}