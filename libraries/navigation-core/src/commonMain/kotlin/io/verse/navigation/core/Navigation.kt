package io.verse.navigation.core

import io.tagd.arch.scopable.library.AbstractLibrary
import io.tagd.arch.scopable.library.Library
import io.tagd.di.Scope
import io.tagd.di.bind
import io.verse.deeplink.core.DeepLinking

class Navigation(name: String, outerScope: Scope) : AbstractLibrary(name, outerScope) {

    var registry: DestinationRegistry? = null
        private set

    var deepLinking: DeepLinking? = null
        private set

    override fun release() {
        registry = null
        deepLinking = null
        super.release()
    }

    class Builder : Library.Builder<Navigation>() {

        private lateinit var registry: DestinationRegistry
        private var deepLinking: DeepLinking? = null

        override fun name(name: String?): Builder {
            this.name = name
            return this
        }

        override fun scope(outer: Scope?): Builder {
            super.scope(outer)
            return this
        }

        fun registry(registry : DestinationRegistry): Builder {
            this.registry = registry
            return this
        }

        fun deeplinking(deepLinking: DeepLinking?): Builder {
            this.deepLinking = deepLinking
            return this
        }

        override fun buildLibrary(): Navigation {
            return Navigation(
                name = name ?: "${outerScope.name}/$NAME",
                outerScope = outerScope
            ).also { library ->

                library.registry = registry
                library.deepLinking = deepLinking

                outerScope.bind<Library, Navigation>(instance = library) // as 3rd party lib offering
            }
        }

        companion object {
            const val NAME = "navigation"
        }
    }
}