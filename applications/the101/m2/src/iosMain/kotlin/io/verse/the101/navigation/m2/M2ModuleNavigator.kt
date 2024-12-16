@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.the101.navigation.m2

import io.tagd.arch.scopable.module.ModuleNavigator
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.deeplink.core.handler.DefaultDeepLinkHandler
import io.verse.the101.navigation.registry.MyAppDestinationRegistry

actual class M2ModuleNavigator actual constructor(
    private val weakContext: WeakReference<Context>,
    module: M2Module,
    handler: DefaultDeepLinkHandler
) : ModuleNavigator<M2Module> {

    override val navigatable: M2Module = module

    private var registry: MyAppDestinationRegistry? = null
    private var deepLinkHandler: DefaultDeepLinkHandler? = handler

    override fun release() {
        TODO("Not yet implemented")
    }
}