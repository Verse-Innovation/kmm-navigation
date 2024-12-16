@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.deeplink.core.sp.facebook

import android.net.Uri
import android.os.Bundle
import com.facebook.applinks.AppLinkData
import com.facebook.bolts.AppLinks
import io.tagd.langx.Callback
import io.tagd.langx.Context
import io.tagd.langx.Intent
import io.tagd.langx.ref.WeakReference
import io.verse.deeplink.core.gateway.DeferredDeepLinkProviderGateway
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.model.DeepLinkException
import io.verse.deeplink.core.model.DeferredDeepLinkException

actual open class FacebookDynamicLinkGateway actual constructor(
    private val weakContext: WeakReference<Context>,
    override val rank: Int
) : DeferredDeepLinkProviderGateway() {

    override fun fetchIfAny(
        intent: Intent,
        success: Callback<DeepLink>?,
        upgrade: Callback<Intent>?,
        failure: Callback<DeepLinkException>?
    ) {

        fetchAppLink(getTargetUriFromInboundIntent(intent), success, failure)
    }

    override fun isUrlPatternMatches(url: String): Boolean {
        val uri = Uri.parse(url)
        uri.scheme?.let { scheme ->
            return service<FacebookDynamicLinkService>()?.containsAppScheme(scheme) == true
        }
        return false
    }

    override fun resolve(
        url: String,
        success: Callback<DeepLink>?,
        upgrade: Callback<Intent>?,
        failure: Callback<DeepLinkException>?
    ) {

        val uri = Uri.parse(url)
        fetchAppLink(uri, success, failure)
    }

    private fun getTargetUriFromInboundIntent(intent: Intent): Uri? {
        val appLinkData: Bundle? = AppLinks.getAppLinkData(intent)
        if (appLinkData != null) {
            val targetString: String? = appLinkData.getString(EXTRA_KEY_TARGET)
            if (targetString != null) {
                return Uri.parse(targetString)
            }
        }
        return null
    }

    private fun fetchAppLink(
        targetUri: Uri?,
        success: Callback<DeepLink>?,
        failure: Callback<DeepLinkException>?
    ) {

        //check for facebook deep link
        if (targetUri != null) {
            notifyDeepLink(targetUri, success)
        } else {

            //check for deferred facebook app link
            weakContext.get()?.let { context ->
                AppLinkData.fetchDeferredAppLinkData(context) { appLinkData ->
                    if (appLinkData?.targetUri != null) {
                        notifyDeepLink(appLinkData.targetUri!!, success)
                    } else {
                        failure(failure, DeepLinkException("no pending deeplinks found"))
                    }
                }
            } ?: kotlin.run {
                failure(failure, DeepLinkException("context is null"))
            }
        }
    }

    private fun notifyDeepLink(targetUri: Uri, success: Callback<DeepLink>?) {
        resolveAppLink(link = targetUri.toString(), rank = rank, success = success)
    }

    private fun failure(
        callback: Callback<DeepLinkException>?,
        error: Exception
    ) {

        callback?.invoke(DeferredDeepLinkException("failed to fetch deep link", cause = error))
    }

    override fun release() {
        service = null
        super.release()
    }

    companion object {

        private const val EXTRA_KEY_TARGET = "target_url"
    }
}