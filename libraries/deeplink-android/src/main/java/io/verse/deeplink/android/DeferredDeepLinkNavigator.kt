package io.verse.deeplink.android

import io.tagd.langx.Intent
import io.verse.deeplink.core.presentation.IDeferredDeepLinkView
import io.verse.deeplink.core.presentation.IDeferredDeepLinkViewNavigator
import java.lang.ref.WeakReference

open class DeferredDeepLinkNavigator<V : IDeferredDeepLinkView>(
    weakNavigatable: WeakReference<out DeferredDeepLinkActivity<*, *, *>>?
) : DeepLinkNavigator<V>(weakNavigatable), IDeferredDeepLinkViewNavigator<V> {

    override fun continueDefault() {
        (weakNavigatable?.get() as? DeferredDeepLinkActivity<*, *, *>)?.continueDefault()
    }

    override fun navigateToUpgradeApp(intent: Intent) {
        super.navigateToUpgradeApp(intent)
    }

    override fun release() {
        weakNavigatable?.clear()
        weakNavigatable = null
    }

}