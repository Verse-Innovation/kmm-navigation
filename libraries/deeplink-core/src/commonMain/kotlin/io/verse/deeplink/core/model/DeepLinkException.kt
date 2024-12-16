package io.verse.deeplink.core.model

open class DeepLinkException(message: String, cause: Throwable? = null) :
    Exception(message, cause)

class DeferredDeepLinkException(message: String, cause: Throwable? = null) :
    DeepLinkException(message, cause)