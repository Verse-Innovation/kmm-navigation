@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.the101.navigation.m1

import android.content.Intent
import io.tagd.arch.access.library
import io.tagd.arch.scopable.module.ModuleNavigator
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.deeplink.core.handler.DefaultDeepLinkHandler
import io.verse.deeplink.core.model.DeepLink
import io.verse.navigation.core.Destination
import io.verse.navigation.core.Navigation
import io.verse.navigation.core.destinationOf
import io.verse.navigation.core.nextRemainingPath
import io.verse.the101.navigation.m1.view.FragmentHome
import io.verse.the101.navigation.m1.view.HomeActivity
import io.verse.the101.navigation.registry.MyAppDestinationRegistry

actual class M1ModuleNavigator actual constructor(
    private val weakContext: WeakReference<Context>,
    module: M1Module,
    handler: DefaultDeepLinkHandler
) : ModuleNavigator<M1Module> {

    private var registry: MyAppDestinationRegistry? = null
    private var deepLinkHandler: DefaultDeepLinkHandler? = handler
    override val navigatable: M1Module = module

    init {
        val library = library<Navigation>()
        registry = library?.registry as? MyAppDestinationRegistry
        registerPaths()

        registerDeepLinks()
    }

    private fun registerPaths() {
        val defaultPath = destinationOf(
            navigatable.name,
            MyAppDestinationRegistry.M1Destinations.DEFAULT_PATH,
            HomeActivity::class
        )
        val homePath = destinationOf(
            navigatable.name,
            MyAppDestinationRegistry.M1Destinations.PATH_TO_M1_HOME,
            HomeActivity::class
        )
        val homeFragmentPath = destinationOf(
            navigatable.name,
            MyAppDestinationRegistry.M1Destinations.PATH_TO_M1_HOME_FRAGMENT,
            HomeActivity::class,
            FragmentHome::class
        )
        registry?.register(defaultPath, homePath, homeFragmentPath)
    }

    private fun registerDeepLinks() {
        registerHome()
    }

    private fun registerHome() {
        val implicitPaths = listOf(
            MyAppDestinationRegistry.M1Destinations.DEFAULT_PATH,
            MyAppDestinationRegistry.M1Destinations.PATH_TO_M1_HOME,
            MyAppDestinationRegistry.M1Destinations.PATH_TO_M1_HOME_FRAGMENT
        )

        implicitPaths.forEach { implicitPath ->
            deepLinkHandler?.register(implicitPath) { link, result ->
                result.invoke(link.resolvedImplicitIntents())
            }
        }
    }

    private fun DeepLink.resolvedImplicitIntents(): List<Intent> {
        return weakContext.get()?.let { context ->
            val intents = arrayListOf<Intent>()
            path?.let { path ->
                registry?.destination(path)?.let { navigatablePath ->
                    val intent = Intent(
                        context,
                        Class.forName(navigatablePath.components[0])
                    )
                    intent.putExtra(
                        Destination.REMAINING_PATH,
                        navigatablePath.pathAsAction.nextRemainingPath()
                    )
                    intent.putStringArrayListExtra(
                        Destination.REMAINING_COMPONENTS,
                        arrayListOf<String>().apply {
                            addAll(navigatablePath.components)
                            removeAt(0)
                        }
                    )
                    intents.add(intent)
                }
            }
            intents
        } ?: kotlin.run {
            listOf()
        }
    }

    override fun release() {
        deepLinkHandler = null
        registry = null
    }
}