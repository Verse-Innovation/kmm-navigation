package io.verse.deeplink.splash.android.spec.presentation

import io.tagd.arch.access.library
import io.verse.deeplink.core.DeepLinking
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.presentation.DeferredDeepLinkPresenter
import io.verse.deeplink.core.presentation.IDeferredDeepLinkView
import io.verse.deeplink.core.presentation.IDeferredDeepLinkViewNavigator


interface ISplashView : IDeferredDeepLinkView {
    fun showToast(s: String)
}

class SplashPresenter(
    view: ISplashView,
    navigator: IDeferredDeepLinkViewNavigator<ISplashView>
) : DeferredDeepLinkPresenter<
        ISplashView,
    IDeferredDeepLinkViewNavigator<ISplashView>
>(
    view = view,
    navigator = navigator
) {

//    private var deepLink: DeepLink? = null
//    private var handleActualLinkPrerequisites: Boolean = false
//
//    fun initWith(deepLink: DeepLink? = null) {
//        this.deepLink = deepLink
//        deepLink?.let {
//            handleActualLinkPrerequisites = it.queryParameters["actual"] != null
//        }
//    }

    override fun shouldFetchDeferredDeepLinkIfAny(): Boolean = true

    override fun getDefaultDeepLink(): DeepLink {
        return DeepLink(
            url = "verse://deepLinkSample/m1/home",
            minSupportedVersion = 1
        )
    }

    override fun onContinueDefault() {
//        if (handleActualLinkPrerequisites) {
//
//            onActualLinkPrerequisitesComplete()
//        } else {
//            navigateToNext()
//        }
        navigateToNext()
    }

    private fun navigateToNext() {
        //no-op
    }

    private fun onActualLinkPrerequisitesComplete() {
//        checkIfUnhandledLinkCanBeHandled(
//            DeepLink(
//                deepLink!!.queryParameters["actual"] as String,
//                1
//            )
//        )
    }

//    private fun checkIfUnhandledLinkCanBeHandled(deepLink: DeepLink) {
//        library<DeepLinking>()?.let { deepLinking ->
//            val canHandle = deepLinking.canDispatch(deepLink)
//            if (canHandle) {
//                deepLinking.dispatch(deepLink, result = {
//                    view?.showToast("successfully redirecting")
//                }, error = {
//                    view?.showToast("permanently broken")
//                    navigateToNext()
//                })
//            }
//        }
//    }
}

