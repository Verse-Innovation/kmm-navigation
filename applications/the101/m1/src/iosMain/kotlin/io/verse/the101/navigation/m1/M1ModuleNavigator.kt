package io.verse.the101.navigation.m1

import io.tagd.arch.access.library
import io.tagd.arch.scopable.module.ModuleNavigator
import io.tagd.arch.present.mvnp.Navigatable
import io.tagd.langx.Context
import io.tagd.langx.Intent
import io.tagd.langx.ref.WeakReference
import io.verse.deeplink.core.handler.DefaultDeepLinkHandler
import io.verse.navigation.core.Navigation
import io.verse.the101.navigation.registry.MyAppDestinationRegistry
import kotlin.reflect.KClass

actual class M1ModuleNavigator actual constructor(
    private val weakContext: WeakReference<Context>,
    module: M1Module,
    handler: DefaultDeepLinkHandler
) : ModuleNavigator<M1Module> {

    private var registry: MyAppDestinationRegistry? = null
    private var deepLinkHandler: DefaultDeepLinkHandler? = handler
    override val navigatable: M1Module = module

    override fun release() {
        TODO("Not yet implemented")
    }
}