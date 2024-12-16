@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.deeplink.core.sp.firebase

import android.content.Context
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import io.tagd.arch.access.reference
import io.tagd.arch.control.IApplication
import io.tagd.arch.domain.crosscutting.async.compute
import io.tagd.langx.Callback
import io.tagd.arch.infra.ReferenceHolder
import io.tagd.langx.Intent
import io.verse.deeplink.core.gateway.DeferredDeepLinkProviderGateway
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.model.DeepLinkException
import io.verse.deeplink.core.model.DeferredDeepLinkException

actual open class FirebaseDynamicLinkGateway actual constructor(
    override val rank: Int
) : DeferredDeepLinkProviderGateway() {

    override fun fetchIfAny(
        intent: Intent,
        success: Callback<DeepLink>?,
        upgrade: Callback<Intent>?,
        failure: Callback<DeepLinkException>?
    ) {

        val task = newDynamicLinkTask(intent)
        fetchDynamicLink(task, success, upgrade, failure)
    }

    private fun newDynamicLinkTask(intent: Intent) = Firebase.dynamicLinks.getDynamicLink(intent)

    override fun isUrlPatternMatches(url: String): Boolean {
        val uri = Uri.parse(url)
        return uri.scheme?.startsWith("http") == true
                && service<FirebaseDynamicLinkService>()?.containsDynamicHost(uri.host!!) == true
    }

    override fun resolve(
        url: String,
        success: Callback<DeepLink>?,
        upgrade: Callback<Intent>?,
        failure: Callback<DeepLinkException>?
    ) {

        val uri = Uri.parse(url)
        val task = newDynamicLinkTask(uri)
        fetchDynamicLink(task, success, upgrade, failure)
    }

    private fun newDynamicLinkTask(uri: Uri): Task<PendingDynamicLinkData> =
        Firebase.dynamicLinks.getDynamicLink(uri)

    private fun fetchDynamicLink(
        task: Task<PendingDynamicLinkData>,
        success: Callback<DeepLink>?,
        upgrade: Callback<Intent>?,
        failure: Callback<DeepLinkException>?
    ) {

        task.addOnSuccessListener { pendingDynamicLinkData ->
            compute {
                if (pendingDynamicLinkData != null) {
                    success(pendingDynamicLinkData, success, upgrade, failure)
                } else {
                    failure(failure, DeepLinkException("no pending deeplinks found"))
                }
            }
        }.addOnFailureListener { exception ->
            compute {
                failure(failure, exception)
            }
        }
    }

    private fun success(
        pendingDynamicLinkData: PendingDynamicLinkData,
        success: Callback<DeepLink>?,
        upgrade: Callback<Intent>?,
        failure: Callback<DeepLinkException>?
    ) {

        if (pendingDynamicLinkData.minimumAppVersion > currentAppVersion()) {
            notifyUpgradeApp(pendingDynamicLinkData, upgrade, failure)
        } else {
            notifyDeepLink(pendingDynamicLinkData, success)
        }
    }

    private fun notifyUpgradeApp(
        pendingDynamicLinkData: PendingDynamicLinkData,
        upgrade: Callback<Intent>?,
        failure: Callback<DeepLinkException>?
    ) {

        val context = reference<IApplication, ReferenceHolder<IApplication>>() as Context
        val updateIntent = pendingDynamicLinkData.getUpdateAppIntent(context)
        if (updateIntent != null) {
            upgrade?.invoke(updateIntent)
        } else {
            failure?.invoke(DeferredDeepLinkException("failed to fetch upgrade intent"))
        }
    }

    private fun notifyDeepLink(
        pendingDynamicLinkData: PendingDynamicLinkData,
        success: Callback<DeepLink>?
    ) {

        val appLink = pendingDynamicLinkData.link.toString()
        resolveAppLink(link = appLink, rank = rank, success = success)
    }

    private fun currentAppVersion() =
        service<FirebaseDynamicLinkService>()?.currentAppVersion() ?: 1

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

}