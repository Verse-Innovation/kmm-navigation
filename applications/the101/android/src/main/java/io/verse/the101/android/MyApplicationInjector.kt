package io.verse.the101.android

import io.tagd.android.app.TagdApplicationInjector
import io.tagd.arch.scopable.WithinScopableInitializer
import io.tagd.core.dependencies
import io.tagd.di.Scope
import io.tagd.langx.Callback
import io.verse.architectures.soa.SoaInitializer
import io.verse.the101.navigation.m1.M1ModuleInitializer
import io.verse.the101.navigation.m2.M2ModuleInitializer
import io.verse.the101.navigation.m3.M3ModuleInitializer

class MyApplicationInjector(application: MyApplication) :
    TagdApplicationInjector<MyApplication>(application) {

    override fun load(initializers: ArrayList<WithinScopableInitializer<MyApplication, *>>) {
        super.load(initializers)
        initializers.add(SoaInitializer(within))
        initializers.add(MyDeepLinkingInitializer(within))
        initializers.add(MyNavigationInitializer(within))
    }

    override fun initialize(callback: Callback<Unit>) {
        super.initialize {
            setupModules()
            callback.invoke(Unit)
        }
    }

    private fun setupModules() {
        within.let { app ->
            val productScope = Scope(name = "deeplink_sample").also { productScope ->
                app.thisScope.addSubScope(productScope)
            }

            M1ModuleInitializer().new(
                dependencies = dependencies(
                    M1ModuleInitializer.ARG_CONTEXT to app,
                    M1ModuleInitializer.ARG_SCOPE to productScope
                )
            )
            M2ModuleInitializer().new(
                dependencies = dependencies(
                    M2ModuleInitializer.ARG_CONTEXT to app,
                    M1ModuleInitializer.ARG_SCOPE to productScope
                )
            )
            M3ModuleInitializer().new(
                dependencies = dependencies(
                    M2ModuleInitializer.ARG_CONTEXT to app,
                    M1ModuleInitializer.ARG_SCOPE to productScope
                )
            )
            SplashModule.new(app, productScope)
        }
    }
}
