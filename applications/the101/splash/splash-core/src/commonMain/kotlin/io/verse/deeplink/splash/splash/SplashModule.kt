package io.verse.deeplink.splash.splash

import io.tagd.arch.scopable.module.AbstractModule
import io.tagd.arch.scopable.module.Module
import io.tagd.arch.scopable.module.NavigatableModule
import io.tagd.di.Scope
import io.tagd.di.bind
import io.verse.deeplink.core.handler.DefaultDeepLinkHandler

class SplashModule private constructor(
    name: String,
    outerScope: Scope
) : AbstractModule(name, outerScope), NavigatableModule {

    override var navigator: AbstractSplashModuleNavigator? = null

    var handler: DefaultDeepLinkHandler? = null

    override fun release() {
        navigator = null
        handler = null
    }

    class Builder : Module.Builder<SplashModule>() {

        override fun buildModule(): SplashModule {
            return SplashModule(name ?: NAME, outerScope).also {
                outerScope.bind<Module, SplashModule>(instance = it) // as 3rd party lib offering
            }
        }
    }

    companion object {

        const val NAME = "splash"

        fun new(parent: Scope): SplashModule {
            return Builder().name(NAME).scope(parent).build()
        }
    }
}