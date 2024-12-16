@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.the101.navigation.m3

import android.app.Activity
import android.content.Intent
import androidx.core.os.bundleOf
import io.tagd.arch.scopable.module.ModuleNavigator
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.navigation.core.AbstractDeepLinkNavigator
import io.verse.navigation.core.Destination
import io.verse.the101.navigation.m3.view.ActivityA
import io.verse.the101.navigation.registry.MyAppDestinationRegistry.M3Destinations

actual class M3ModuleNavigator actual constructor(
    private val weakContext: WeakReference<Context>,
    handle: String
) : AbstractDeepLinkNavigator<M3Module>(weakContext = weakContext, handle = handle),
    ModuleNavigator<M3Module> {

    internal var internalNavigator = M3ModuleInternalNavigator(handle)

    override fun registerDestinations(outPaths: ArrayList<String>) {
        registry?.register(
            destinationOf(
                pathAsAction = outPaths.include(M3Destinations.PATH_TO_M3_A),
                ActivityA::class
            )
        )
    }

    fun navigateTo(
        from: Activity,
        toComponentChain: List<String> = listOf(),
        vararg args: Pair<String, Any>
    ) {

        assert(toComponentChain.isNotEmpty())
        val intent = intentTo(toComponentChain)
        intent?.let {
            intent.putExtra(Destination.ARGUMENTS, bundleOf(*args))
            from.startActivity(intent)
        }
    }

    private fun intentTo(componentChain: List<String> = listOf()): Intent? {
        assert(componentChain.isNotEmpty())

        return weakContext.get()?.let { context ->
            val intent = Intent(
                context,
                Class.forName(componentChain[0])
            )
            intent.putStringArrayListExtra(
                Destination.REMAINING_COMPONENTS,
                arrayListOf<String>().apply {
                    addAll(componentChain)
                    removeAt(0)
                }
            )
            intent
        } ?: kotlin.run {
            null
        }
    }
}