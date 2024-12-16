@file:Suppress("EXPECT_AND_ACTUAL_IN_THE_SAME_MODULE")

package io.verse.the101.navigation.m2

import io.tagd.arch.scopable.module.ModuleNavigator
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.deeplink.core.handler.DefaultDeepLinkHandler

expect class M2ModuleNavigator(
    weakContext: WeakReference<Context>,
    module: M2Module,
    handler: DefaultDeepLinkHandler
) : ModuleNavigator<M2Module>