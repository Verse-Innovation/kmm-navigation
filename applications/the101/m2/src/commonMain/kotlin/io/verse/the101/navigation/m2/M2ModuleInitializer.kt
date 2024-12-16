package io.verse.the101.navigation.m2

import io.tagd.arch.scopable.module.Module
import io.tagd.core.Dependencies
import io.tagd.core.Initializer
import io.tagd.di.Scope
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference

class M2ModuleInitializer : Initializer<Module> {

    override fun new(dependencies: Dependencies): Module {
        return M2Module.M2ModuleBuilder()
            .context(WeakReference(dependencies.get<Context>(ARG_CONTEXT)!!))
            .scope(dependencies.get<Scope>(ARG_SCOPE)!!)
            .build()
    }

    override fun release() {
    }

    companion object {

        const val ARG_CONTEXT = "context"
        const val ARG_SCOPE = "scope"
    }
}