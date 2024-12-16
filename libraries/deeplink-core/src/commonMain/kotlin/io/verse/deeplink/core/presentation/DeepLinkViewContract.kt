package io.verse.deeplink.core.presentation

import io.tagd.arch.domain.crosscutting.async.AsyncContext
import io.tagd.arch.present.mvnp.NavigatablePresenter
import io.tagd.arch.present.mvnp.NavigatableView
import io.tagd.arch.present.mvnp.Navigator
import io.tagd.arch.present.mvp.Presenter
import io.tagd.langx.Intent
import io.verse.deeplink.core.model.DeepLink

interface IDeepLinkView : NavigatableView, AsyncContext

interface IDeepLinkViewNavigator<V : IDeepLinkView> : Navigator<V> {

    fun navigateToDeepLink(deepLink: DeepLink)

    fun navigateToUpgradeApp(intent: Intent)

    fun navigateToLauncher()

    fun navigateToCaller()
}

interface IDeepLinkPresenter<V : IDeepLinkView, N : Navigator<V>> : Presenter<V>,
    NavigatablePresenter<V, N> {

    fun initWith(url: String?)

    fun onValidDeepLink()
}