package io.verse.deeplink.core.presentation

import io.tagd.arch.access.library
import io.tagd.arch.domain.usecase.Args
import io.tagd.arch.domain.usecase.argsOf
import io.tagd.arch.scopable.library.usecase
import io.tagd.arch.present.mvnp.NavigatableLifeCycleAwarePresenter
import io.tagd.langx.Intent
import io.verse.deeplink.core.DeepLinking
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.usecase.FetchDeferredDeepLinkIfAnyUsecase

abstract class DeferredDeepLinkPresenter<
        V : IDeferredDeepLinkView,
        N : IDeferredDeepLinkViewNavigator<V>
>(view: V, navigator: N) : NavigatableLifeCycleAwarePresenter<V, N>(
    view = view,
    navigator = navigator
), IDeferredDeepLinkPresenter<V, N> {

    override fun onFetchDeferredDeepLinkIfAny(intent: Intent) {
        if (shouldFetchDeferredDeepLinkIfAny()) {
            library<DeepLinking>()?.usecase<FetchDeferredDeepLinkIfAnyUsecase>()?.execute(
                args = argsOf(
                    Args.CONTEXT to this,
                    FetchDeferredDeepLinkIfAnyUsecase.ARG_INTENT to intent
                ),
                success = {
                    onFoundDeepLink(it)
                },
                upgrade = {
                    onUpgradeApp(it)
                },
                failure = {
                    onNotFoundDeferredDeepLink(it)
                }
            )
        } else {
            triggerDefaultFlowOrThrow()
        }
    }

    abstract fun shouldFetchDeferredDeepLinkIfAny(): Boolean

    protected open fun onFoundDeepLink(deepLink: DeepLink) {
        navigator?.navigateToDeepLink(deepLink)
    }

    private fun onUpgradeApp(it: Intent) {
        navigator?.navigateToUpgradeApp(it)
    }

    protected open fun onNotFoundDeferredDeepLink(error: Throwable) {
        error.printStackTrace()
        triggerDefaultFlowOrThrow()
    }

    protected open fun triggerDefaultFlowOrThrow() {
        navigator?.continueDefault()
    }

    abstract fun getDefaultDeepLink(): DeepLink?
}

