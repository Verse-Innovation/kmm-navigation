package io.verse.deeplink.core.presentation

import io.tagd.arch.access.library
import io.tagd.arch.domain.crosscutting.async.compute
import io.tagd.arch.domain.usecase.Args
import io.tagd.arch.domain.usecase.UseCase
import io.tagd.arch.domain.usecase.argsOf
import io.tagd.arch.scopable.library.usecase
import io.tagd.arch.present.mvnp.NavigatableLifeCycleAwarePresenter
import io.verse.deeplink.core.DeepLinking
import io.verse.deeplink.core.usecase.ResolveDeepLinkUsecase

open class DeepLinkPresenter(view: IDeepLinkView, navigator: IDeepLinkViewNavigator<IDeepLinkView>) :
    NavigatableLifeCycleAwarePresenter<IDeepLinkView, IDeepLinkViewNavigator<IDeepLinkView>>(
        view = view,
        navigator = navigator
    ), IDeepLinkPresenter<IDeepLinkView, IDeepLinkViewNavigator<IDeepLinkView>> {

    private var usecase = library<DeepLinking>()?.usecase<ResolveDeepLinkUsecase>()

    private var url: String? = null

    override fun initWith(url: String?) {
        this.url = url
    }

    override fun onValidDeepLink() {
        compute {
            url?.let {
                resolveDeepLink(it)
            } ?: run {
                navigator?.navigateToLauncher()
            }
        }
    }

    private fun resolveDeepLink(url: String) {
        usecase?.execute(
            args = argsOf(
                Args.CONTEXT to this,
                ResolveDeepLinkUsecase.ARG_URL to url
            ),
            success = {
                navigator?.navigateToDeepLink(it)
            },
            upgrade = {
                navigator?.navigateToUpgradeApp(it)
            },
            failure = {
                navigator?.navigateToLauncher()
            }
        )
    }

    override fun onBackPressed() {
        navigator?.navigateToCaller()
        super.onBackPressed()
    }

    override fun release() {
        usecase?.cancel(this)
        usecase = null
        super.release()
    }
}