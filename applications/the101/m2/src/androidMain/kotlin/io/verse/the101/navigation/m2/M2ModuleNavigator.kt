@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.the101.navigation.m2

import io.tagd.arch.access.library
import io.tagd.arch.scopable.module.ModuleNavigator
import io.tagd.langx.Context
import io.tagd.langx.Intent
import io.tagd.langx.ref.WeakReference
import io.verse.deeplink.core.handler.DefaultDeepLinkHandler
import io.verse.deeplink.core.model.DeepLink
import io.verse.navigation.core.Destination
import io.verse.navigation.core.Navigation
import io.verse.navigation.core.destinationOf
import io.verse.navigation.core.nextRemainingPath
import io.verse.the101.navigation.m2.view.FragmentFour
import io.verse.the101.navigation.m2.view.FragmentOne
import io.verse.the101.navigation.m2.view.FragmentTwo
import io.verse.the101.navigation.m2.view.SettingsActivity
import io.verse.the101.navigation.registry.MyAppDestinationRegistry

actual class M2ModuleNavigator actual constructor(
    private val weakContext: WeakReference<Context>,
    module: M2Module,
    handler: DefaultDeepLinkHandler
) : ModuleNavigator<M2Module> {

    override val navigatable: M2Module = module

    private var registry: MyAppDestinationRegistry? = null
    private var deepLinkHandler: DefaultDeepLinkHandler? = handler

    init {
        val library = library<Navigation>()
        registry = library?.registry as? MyAppDestinationRegistry

        registerPaths()
        registerDeepLinks()
    }

    private fun registerPaths() {
        val settingsFragmentOnePath = destinationOf(
            navigatable.name,
            MyAppDestinationRegistry.M2Destinations.PATH_TO_M2_SETTINGS_FRAGMENT_1,
            //if we mention the view spec/contract class here, then the corresponding implementation
            //class can register its class name as the implementer class, the registry can pick
            //the implementation class to resolve the path. In that way, we can make this code also
            //common for all the kmm-supported-platforms.
            SettingsActivity::class,
            FragmentOne::class
        )
        registry?.register(settingsFragmentOnePath)

        val settingsFragmentTwoFragmentFourPath = destinationOf(
            navigatable.name,
            MyAppDestinationRegistry.M2Destinations.PATH_TO_M2_SETTINGS_FRAGMENT_2_FRAGMENT_4,
            SettingsActivity::class,
            FragmentTwo::class,
            FragmentFour::class
        )
        registry?.register(settingsFragmentTwoFragmentFourPath)

        registry?.register(
            MyAppDestinationRegistry.M2Destinations.PATH_TO_M2_SETTINGS
        )
        registry?.register(
            MyAppDestinationRegistry.M2Destinations.PATH_TO_M2_SETTINGS_FRAGMENT_2
        )

        registry?.register(
            MyAppDestinationRegistry.M2Destinations.PATH_TO_M2_SETTINGS_FRAGMENT_2_FRAGMENT_3
        )
    }

    private fun registerDeepLinks() {
        registerSettings()
        registerSettingsFragmentTwo()
        registerSettingsFragmentTwoFragmentThree()
        registerSettingsFragmentTwoFragmentFour()
    }

    private fun registerSettings() {
        deepLinkHandler?.register(
            MyAppDestinationRegistry.M2Destinations.PATH_TO_M2_SETTINGS.pathAsAction,
        ) { link, result ->
            result.invoke(createIntents(link))
        }
    }

    private fun registerSettingsFragmentTwo() {
        deepLinkHandler?.register(
            MyAppDestinationRegistry.M2Destinations.PATH_TO_M2_SETTINGS_FRAGMENT_2.pathAsAction
        ) { link, result ->
            result.invoke(createIntents(link))
        }
    }

    private fun registerSettingsFragmentTwoFragmentThree() {
        deepLinkHandler?.register(
            MyAppDestinationRegistry.M2Destinations.PATH_TO_M2_SETTINGS_FRAGMENT_2_FRAGMENT_3
                .pathAsAction
        ) { link, result ->
            result.invoke(createIntents(link))
        }
    }

    private fun registerSettingsFragmentTwoFragmentFour() {
        deepLinkHandler?.register(
            MyAppDestinationRegistry.M2Destinations.PATH_TO_M2_SETTINGS_FRAGMENT_2_FRAGMENT_4
        ) { link, result ->
            result.invoke(createIntents(link))
        }
    }

    private fun createIntents(link: DeepLink) = weakContext.get()?.let {
        val intents = arrayListOf<Intent>()
        link.path?.let { path ->
            registry?.destination(path)?.let { navigatablePath ->
                val intent = Intent(
                    it,
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

    override fun release() {
        registry = null
        deepLinkHandler = null
    }
}