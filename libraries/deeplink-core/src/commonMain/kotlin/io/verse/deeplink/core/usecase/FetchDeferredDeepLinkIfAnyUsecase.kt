package io.verse.deeplink.core.usecase

import io.tagd.arch.access.library
import io.tagd.arch.domain.usecase.Args
import io.tagd.langx.Callback
import io.tagd.arch.domain.usecase.LiveUseCase
import io.tagd.langx.Intent
import io.verse.deeplink.core.DeepLinking
import io.verse.deeplink.core.DeferredDeepLinkServiceAggregator
import io.verse.deeplink.core.model.DeepLink

class FetchDeferredDeepLinkIfAnyUsecase : LiveUseCase<Pair<DeepLink?, Intent?>>() {

    fun execute(
        args: Args?,
        triggering: Callback<Unit>? = null,
        success: Callback<DeepLink>? = null,
        upgrade: Callback<Intent>? = null,
        failure: Callback<Throwable>? = null
    ) {

        execute(args, triggering = triggering, success = { result ->
            result.first?.let { deepLink ->
                success?.invoke(deepLink)
            } ?: result.second?.let { intent ->
                upgrade?.invoke(intent)
            }
        }, failure)
    }

    override fun trigger(args: Args) {
        val intent = args.get<Intent>(ARG_INTENT)
        intent?.let {
            deferredDeepLinkService()?.fetchIfAny(intent, success = {
                setValue(args, Pair(it, null))
            }, upgrade = {
                setValue(args, Pair(null, it))
            }, failure = {
                setError(args, it)
            })
        }
    }

    private fun deferredDeepLinkService(): DeferredDeepLinkServiceAggregator? {
        return library<DeepLinking>()?.deferredDeepLinkServiceSpec
    }

    companion object {
        const val ARG_INTENT = "intent"
    }
}