package io.verse.navigation.core

import androidx.core.os.bundleOf
import io.tagd.arch.domain.crosscutting.converter.Converter
import io.tagd.langx.Context
import io.tagd.langx.Intent
import io.tagd.langx.ref.WeakReference
import io.verse.deeplink.core.model.DeepLink

class DeepLinkToIntentConverter(
    private val weakContext: WeakReference<Context>,
    private val registry: DestinationRegistry
) : Converter<DeepLink, List<Intent>> {

    override fun convert(value: DeepLink): List<Intent> {
        return weakContext.get()?.let { context ->
            value.toImplicitIntents(context)
        } ?: kotlin.run {
            listOf()
        }
    }

    private fun DeepLink.toImplicitIntents(
        context: Context
    ): ArrayList<android.content.Intent> {

        val intents = arrayListOf<android.content.Intent>()
        path?.let { path ->
            toImplicitIntents(path, context, intents)
        }
        return intents
    }

    private fun DeepLink.toImplicitIntents(
        path: String,
        context: Context,
        intents: ArrayList<android.content.Intent>
    ) {

        registry.destination(path)?.let { destinationPath ->
            intents.add(newDestinationIntent(context, destinationPath))
        }
    }

    private fun DeepLink.newDestinationIntent(
        context: Context,
        navigatablePath: Destination
    ): android.content.Intent {

        val intent = android.content.Intent(
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

        val params = queryParameters.entries.map { it.key to it.value }.toTypedArray()
        intent.putExtra(Destination.ARGUMENTS, bundleOf(*params))

        return intent
    }
}