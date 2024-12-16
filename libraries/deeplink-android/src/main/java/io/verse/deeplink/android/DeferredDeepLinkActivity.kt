package io.verse.deeplink.android

import android.os.Bundle
import io.verse.deeplink.core.presentation.IDeferredDeepLinkPresenter
import io.verse.deeplink.core.presentation.IDeferredDeepLinkView
import io.verse.deeplink.core.presentation.IDeferredDeepLinkViewNavigator
import io.verse.navigation.android.NavigatableDestinationActivity

abstract class DeferredDeepLinkActivity<
    V : IDeferredDeepLinkView,
    N : IDeferredDeepLinkViewNavigator<V>,
    P : IDeferredDeepLinkPresenter<V, N>
> : NavigatableDestinationActivity<V, N, P>(),
    IDeferredDeepLinkView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onNewIntent(intent)
    }

    override fun onReady() {
        fetchDeferredDeepLinkIfAny()
    }

    protected open fun fetchDeferredDeepLinkIfAny() {
        presenter?.onFetchDeferredDeepLinkIfAny(intent)
    }

    open fun continueDefault() {
        presenter?.onContinueDefault()
    }
}
