package io.verse.deeplink.core.sp.inhouse

import io.verse.architectures.soa.provider.DefaultServiceProvider
import io.verse.deeplink.core.DeepLinkServiceProvider

class DefaultDeepLinkServiceProvider(
    genre: String = GENRE
) : DefaultServiceProvider(genre = genre), DeepLinkServiceProvider {

    companion object {
        const val GENRE = "deep-linking/deep-link"
    }
}