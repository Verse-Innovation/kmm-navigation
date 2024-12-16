package io.verse.deeplink.android

import android.app.Activity
import android.content.Context
import android.os.Bundle
import io.tagd.arch.domain.crosscutting.async.present
import io.tagd.di.injectX
import io.tagd.langx.Intent
import io.verse.deeplink.core.DeepLinking
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.presentation.IDeepLinkView
import io.verse.deeplink.core.presentation.IDeepLinkViewNavigator
import java.lang.ref.WeakReference

open class DeepLinkNavigator<V : IDeepLinkView>(
    protected var weakNavigatable: WeakReference<out Activity>?
) : IDeepLinkViewNavigator<V> {

    override val navigatable: V?
        get() = weakNavigatable?.get() as? V

    private var deepLinking by injectX<DeepLinking>()

    override fun navigateToDeepLink(deepLink: DeepLink) {
        present {
            weakNavigatable?.get()?.navigateToDeeLink(deepLink)
        }
    }

    private fun Activity.navigateToDeeLink(deepLink: DeepLink) {
        intent.data = null

        deepLinking?.dispatch(deepLink, result = { intents ->
            navigateToIntent(intents)
        }, error = {
            navigateToSplash()
        })

        finish()
    }

    private fun Activity.navigateToIntent(intents: List<Intent>) {
        val lastIntent = intents.last().apply {
            forceLookupImplicitIntentWithinCalleeApplication(applicationContext)
        }

        // Copy source intent's extras to lastIntent intent without over-writing the existing
        // extras in lastIntent intent.
        lastIntent.putAllWithoutOverwrite(intent)

        startActivity(lastIntent)
    }

    override fun navigateToUpgradeApp(intent: Intent) {
        present {
            weakNavigatable?.get()?.navigateToUpgradeApp(intent)
        }
    }

    private fun Activity.navigateToUpgradeApp(intent: Intent) {
        startActivity(intent)
        finish()
    }

    override fun navigateToLauncher() {
        present {
            weakNavigatable?.get()?.navigateToSplash()
        }
    }

    private fun Activity.navigateToSplash() {
        //todo
        finish()
    }

    override fun navigateToCaller() {
        present {
            weakNavigatable?.get()?.finish()
        }
    }

    override fun release() {
        weakNavigatable?.clear()
        weakNavigatable = null
        deepLinking = null
    }
}

/**
 * If this is an Implicit Intent, setting packageName will force Android to look for intent-filter
 * for this intent inside the client app.
 */
internal fun Intent.forceLookupImplicitIntentWithinCalleeApplication(context: Context) {
    if (action != Intent.ACTION_VIEW) {
        `package` = context.packageName
    }
}

/**
 * Copies extras from [source] Intent to target Intent without overwriting the keys which already
 * exists in the target Intent.
 *
 * Note: This method modifies the extras in [source] Intent.
 * [Bundle] has bundle.deepcopy() method but that requires API 26 so going with this solution for
 * now because in case of DeeplinkActivity, it will be finished just after launching the intent with
 * target bundle.
 *
 * Warning: Use this method with caution as it modifies the extras in [source] Intent.
 */
internal fun Intent.putAllWithoutOverwrite(source: Intent) {
    val targetBundle: Bundle = this.extras ?: Bundle()
    val targetKeys: Set<String> = targetBundle.keySet()

    val sourceBundle: Bundle = source.extras ?: Bundle()
    val sourceKeys: Set<String> = sourceBundle.keySet()

    // Remove duplicate extras from source bundle, to avoid overwrite in target intent.
    sourceKeys
        .filter { key -> targetKeys.contains(key) }
        .forEach { key -> sourceBundle.remove(key) }

    targetBundle.putAll(sourceBundle)
    this.putExtras(targetBundle)
}