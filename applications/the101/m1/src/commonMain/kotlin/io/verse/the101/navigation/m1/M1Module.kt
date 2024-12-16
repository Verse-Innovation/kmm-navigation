package io.verse.the101.navigation.m1

import io.tagd.arch.access.library
import io.tagd.arch.scopable.module.AbstractModule
import io.tagd.arch.scopable.module.Module
import io.tagd.arch.scopable.module.Module.Builder
import io.tagd.arch.scopable.module.ModuleNavigator
import io.tagd.arch.scopable.module.NavigatableModule
import io.tagd.di.Scope
import io.tagd.di.bind
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.deeplink.core.DeepLinking
import io.verse.deeplink.core.handler.DefaultDeepLinkHandler

class M1Module(outerScope: Scope) : AbstractModule("m1", outerScope), NavigatableModule {

    override var navigator: M1ModuleNavigator? = null

    var handler: DefaultDeepLinkHandler? = null
        private set

    class M1ModuleBuilder : Module.Builder<M1Module>() {

        private lateinit var weakContext: WeakReference<Context>

        fun context(weakContext: WeakReference<Context>): M1ModuleBuilder {
            this.weakContext = weakContext
            return this
        }

        override fun inject(bindings: Scope.(M1Module) -> Unit): Builder<M1Module> {
            super.inject(bindings)
            return this
        }

        override fun buildModule(): M1Module {
            return M1Module(outerScope).apply {
                val handler = DefaultDeepLinkHandler(this.name).also {
                    val library = library<DeepLinking>()
                    library?.putDeeplinkHandler(it)
                    this.handler = it
                }
                navigator = M1ModuleNavigator(weakContext, this, handler)
                outerScope.bind<Module, M1Module>(instance = this) // as 3rd party lib offering
            }
        }
    }

    override fun release() {
        navigator = null
        handler = null
    }
}