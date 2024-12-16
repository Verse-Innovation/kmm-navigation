@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.deeplink.splash.splash

import io.tagd.arch.scopable.module.ModuleNavigator
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.navigation.core.AbstractDeepLinkNavigator

actual open class AbstractSplashModuleNavigator actual constructor(
    weakContext: WeakReference<Context>,
    handle: String
) : AbstractDeepLinkNavigator<SplashModule>(weakContext = weakContext, handle = handle),
    ModuleNavigator<SplashModule>