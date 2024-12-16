package io.verse.the101.navigation.m3

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

class M3Module(outerScope: Scope) : AbstractModule("m3", outerScope), NavigatableModule {

    override var navigator: M3ModuleNavigator? = null

    var handler: DefaultDeepLinkHandler? = null
        private set(value) {
            field = value

            value?.let {
                val library = library<DeepLinking>()
                library?.putDeeplinkHandler(value)
            }
        }

    class M3ModuleBuilder : Builder<M3Module>() {

        private lateinit var weakContext: WeakReference<Context>

        fun context(weakContext: WeakReference<Context>): M3ModuleBuilder {
            this.weakContext = weakContext
            return this
        }

        override fun inject(bindings: Scope.(M3Module) -> Unit): Builder<M3Module> {
            super.inject(bindings)
            return this
        }

        override fun buildModule(): M3Module {
            return M3Module(outerScope).also { module ->
                module.navigator = M3ModuleNavigator(weakContext, module.name).also {
                    it.navigatable = module
                    module.handler = it
                }
                outerScope.bind<Module, M3Module>(instance = module) // as 3rd party lib offering
            }
        }
    }

    override fun release() {
        navigator = null
        handler = null
    }
}