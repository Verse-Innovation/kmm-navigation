@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.the101.navigation.m1

import io.tagd.arch.scopable.module.ModuleNavigator
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.deeplink.core.handler.DefaultDeepLinkHandler
import io.verse.the101.navigation.m1.M1Module

expect class M1ModuleNavigator(
    weakContext: WeakReference<Context>,
    module: M1Module,
    handler: DefaultDeepLinkHandler
) : ModuleNavigator<M1Module>