package io.verse.deeplink.core.handler

import io.tagd.langx.Intent
import io.verse.deeplink.core.model.DeepLink

typealias DeepLinkIntentBuilder = (deepLink: DeepLink, result: (List<Intent>) -> Unit) -> Unit