@file:Suppress("EXPECT_AND_ACTUAL_IN_THE_SAME_MODULE")

package io.verse.the101.navigation.m3

import io.tagd.arch.scopable.module.ModuleNavigator
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.navigation.core.AbstractDeepLinkNavigator

expect class M3ModuleNavigator(
    weakContext: WeakReference<Context>,
    handle: String
) : AbstractDeepLinkNavigator<M3Module>, ModuleNavigator<M3Module>