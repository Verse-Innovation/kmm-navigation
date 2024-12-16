package io.verse.deeplink.core.presentation

import io.tagd.arch.present.mvnp.NavigatablePresenter
import io.tagd.arch.present.mvnp.Navigator
import io.tagd.arch.present.mvp.Presenter
import io.tagd.langx.Intent

interface IDeferredDeepLinkView : IDeepLinkView

interface IDeferredDeepLinkViewNavigator<V : IDeferredDeepLinkView> : IDeepLinkViewNavigator<V> {

    fun continueDefault()
}

interface IDeferredDeepLinkPresenter<V : IDeferredDeepLinkView, N : Navigator<V>> : Presenter<V>,
    NavigatablePresenter<V, N> {

    fun onFetchDeferredDeepLinkIfAny(intent: Intent)

    fun onContinueDefault()
}