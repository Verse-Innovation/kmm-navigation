@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.deeplink.splash.splash

import io.tagd.arch.scopable.module.ModuleNavigator
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.deeplink.core.handler.DefaultDeepLinkHandler
import io.verse.navigation.core.AbstractDeepLinkNavigator

expect class AbstractSplashModuleNavigator(
    weakContext: WeakReference<Context>,
    handle: String
) : AbstractDeepLinkNavigator<SplashModule>, ModuleNavigator<SplashModule>