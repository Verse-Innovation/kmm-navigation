package io.verse.the101.navigation.m3

import io.tagd.arch.scopable.module.ModuleNavigator
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.navigation.core.AbstractDeepLinkNavigator

actual class M3ModuleNavigator actual constructor(
    private val weakContext: WeakReference<Context>,
    handle: String
) : AbstractDeepLinkNavigator<M3Module>(weakContext = weakContext, handle = handle),
    ModuleNavigator<M3Module> {

    override fun release() {
        TODO("Not yet implemented")
    }
}