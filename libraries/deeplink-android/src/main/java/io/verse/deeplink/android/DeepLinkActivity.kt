package io.verse.deeplink.android

import android.content.Intent
import android.os.Bundle
import io.tagd.android.app.TagdApplication
import io.tagd.android.mvnp.NavigatableActivity
import io.verse.deeplink.core.presentation.DeepLinkPresenter
import io.verse.deeplink.core.presentation.IDeepLinkPresenter
import io.verse.deeplink.core.presentation.IDeepLinkView
import io.verse.deeplink.core.presentation.IDeepLinkViewNavigator
import java.lang.ref.WeakReference

open class DeepLinkActivity :
    NavigatableActivity<
            IDeepLinkView,
            IDeepLinkViewNavigator<IDeepLinkView>,
            IDeepLinkPresenter<IDeepLinkView, IDeepLinkViewNavigator<IDeepLinkView>>>(),
    IDeepLinkView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onNewIntent(intent)
    }

    override fun onCreateNavigator(
        savedInstanceState: Bundle?
    ): IDeepLinkViewNavigator<IDeepLinkView> {
        return DeepLinkNavigator<IDeepLinkView>(WeakReference(this)).also {
            navigator = it
        }
    }

    override fun onCreatePresenter(
        savedInstanceState: Bundle?
    ): IDeepLinkPresenter<IDeepLinkView, IDeepLinkViewNavigator<IDeepLinkView>> {

        return DeepLinkPresenter(this, navigator!!).also {
            val uri = intent.data
            it.initWith(uri?.toString())
        }
    }

    override fun onCreateView(savedInstanceState: Bundle?) {
        //todo splash view style
        //todo manifest registration
    }

    override fun onReady() {
        handleDeepLink()
    }

    private fun handleDeepLink() {
        if (isOpenedFromRecents()) {
            navigator?.navigateToLauncher()
        } else {
            presenter?.onValidDeepLink()
        }
    }

    private fun isOpenedFromRecents(): Boolean {
        return (intent.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) ==
                Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
    }

    override fun onDestroy() {
        (application as TagdApplication).presenterFactory()?.let { presenterFactory ->
            presenterFactory.get(this::class)?.detach(this)
            presenterFactory.clear(this::class)
        } ?: kotlin.run {
            presenter?.onDestroy()
        }

        super.onDestroy()
    }
}
