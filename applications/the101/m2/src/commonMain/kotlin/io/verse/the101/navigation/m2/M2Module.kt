package io.verse.the101.navigation.m2

import io.tagd.arch.access.library
import io.tagd.arch.scopable.module.AbstractModule
import io.tagd.arch.scopable.module.Module
import io.tagd.arch.scopable.module.Module.Builder
import io.tagd.arch.scopable.module.NavigatableModule
import io.tagd.di.Scope
import io.tagd.di.bind
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.deeplink.core.DeepLinking
import io.verse.deeplink.core.handler.DefaultDeepLinkHandler

class M2Module(outerScope: Scope) : AbstractModule("m2", outerScope), NavigatableModule {

    override var navigator: M2ModuleNavigator? = null

    var handler: DefaultDeepLinkHandler? = null
        private set

    class M2ModuleBuilder : Module.Builder<M2Module>() {

        private lateinit var weakContext: WeakReference<Context>

        fun context(weakContext: WeakReference<Context>): M2ModuleBuilder {
            this.weakContext = weakContext
            return this
        }

        override fun inject(bindings: Scope.(M2Module) -> Unit): Builder<M2Module> {
            super.inject(bindings)
            return this
        }

        override fun buildModule(): M2Module {
            return M2Module(outerScope).apply {
                val handler = DefaultDeepLinkHandler(this.name).also {
                    val library = library<DeepLinking>()
                    library?.putDeeplinkHandler(it)
                    this.handler = it
                }
                navigator = M2ModuleNavigator(weakContext, this, handler)
                outerScope.bind<Module, M2Module>(instance = this) // as 3rd party lib offering
            }
        }
    }

    override fun release() {
        navigator = null
        handler = null
    }
}