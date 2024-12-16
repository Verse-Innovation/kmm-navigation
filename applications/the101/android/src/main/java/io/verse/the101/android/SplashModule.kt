package io.verse.the101.android

import io.tagd.android.app.TagdApplication
import io.tagd.arch.access.library
import io.tagd.di.Scope
import io.tagd.langx.ref.WeakReference
import io.verse.deeplink.core.DeepLinking
import io.verse.deeplink.splash.android.AndroidSplashModuleNavigator
import io.verse.deeplink.splash.splash.SplashModule

class SplashModule {

    companion object {

        fun new(application: TagdApplication, parent: Scope): SplashModule {
            return SplashModule.new(parent).apply {
                /*val handler = SplashDeepLinkHandler(application).also {
                    val library = library<DeepLinking>()
                    library?.putDeeplinkHandler(it)
                    handler = it
                }*/
                navigator = AndroidSplashModuleNavigator(
                    WeakReference(application),
                    this.name,
                ).also {
                    val library = library<DeepLinking>()
                    library?.putDeeplinkHandler(it)
                    this.handler = it
                }
            }
        }
    }
}