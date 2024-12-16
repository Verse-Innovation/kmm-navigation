package io.verse.deeplink.core.handler

import io.tagd.langx.Callback
import io.tagd.langx.Intent
import io.verse.deeplink.core.model.DeepLink
import io.verse.deeplink.core.model.DeepLinkException

open class DefaultDeepLinkHandler(override val handle: String) : DeepLinkHandler {

    private var intentBuilders = hashMapOf<String, DeepLinkIntentBuilder>()

    override fun register(paths: List<String>, builder: DeepLinkIntentBuilder) {
        paths.forEach { path ->
            register(path, builder)
        }
    }

    override fun register(builder: DeepLinkIntentBuilder, vararg paths: String) {
        paths.forEach { path ->
            register(path, builder)
        }
    }

    override fun register(path: String, builder: DeepLinkIntentBuilder) {
        intentBuilders[path] = builder
    }

    override fun handle(
        dataObject: DeepLink,
        result: Callback<List<Intent>>?,
        error: Callback<Throwable>?
    ) {

        if (intentBuilders.contains(dataObject.path)) {
            try {
                intentBuilders[dataObject.path]?.invoke(dataObject) { intents ->
                    if (intents.isNullOrEmpty()) {
                        notifyError(dataObject, error)
                    } else {
                        result?.invoke(intents) ?: notifyError(dataObject, error)
                    }
                }
            } catch (e: Exception) {
                notifyError(dataObject, error)
            }
        } else {
            notifyError(dataObject, error)
        }
    }

    private fun notifyError(dataObject: DeepLink, error: Callback<Throwable>?) {
        val e = DeepLinkException("there is no mapped path to handle $dataObject")
        error?.invoke(e) ?: throw e
    }

    override fun canHandle(dataObject: DeepLink, result: Callback<Boolean>) {
        if (intentBuilders.contains(dataObject.path)) {
            intentBuilders[dataObject.path]?.let {
                try {
                    it.invoke(dataObject) { intents ->
                        result.invoke(!intents.isNullOrEmpty())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    result.invoke(false)
                }
            } ?: result.invoke(false)
        } else {
            result.invoke(false)
        }
    }

    override fun release() {
        intentBuilders.clear()
    }
}