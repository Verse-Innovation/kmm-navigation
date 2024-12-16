@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.deeplink.core.model

import io.tagd.langx.time.UnixEpochInMillis
import io.verse.architectures.soa.io.ServiceDataObject

actual open class DeepLink actual constructor(
    url: String,
    minSupportedVersion: Int
) : ServiceDataObject(), Comparable<DeepLink> {

    private val uri: Any
        get() = TODO("Not yet implemented")

    actual open val minimumSupportedVersion: Int = minSupportedVersion

    actual open val url: String = uri.toString()

    actual open val scheme: String?
        get() = TODO("Not yet implemented")

    actual open val authority: String?
        get() = TODO("Not yet implemented")

    actual open val path: String?
        get() = TODO("Not yet implemented")

    actual open val segments: List<String>
        get() = TODO("Not yet implemented")

    actual open val queryParameters: Map<String, String?> by lazy {
        val map = hashMapOf<String, String?>()
        /*uri.queryParameterNames?.forEach { name ->
            map[name] = uri.getQueryParameter(name)
        }*/
        map
    }

    override fun compareTo(other: DeepLink): Int {
        return rank - other.rank
    }

    /**
     * the time at which deep link is clicked/touched/interacted
     */
    actual open var interactionTime: UnixEpochInMillis
        get() = TODO("Not yet implemented")
        set(value) {}

    /**
     * Let's say there are more than one deeplinks resolved from deferred deeplink mechanisms,
     * then there must be a way which one to consider. The rank is purely business specific
     * - example scenario - prefer facebook deferred deep link over firebase one etc
     */
    actual open var rank: Int
        get() = TODO("Not yet implemented")
        set(value) {}

    actual override var handle: String
        get() = TODO("Not yet implemented")
        set(value) {}

    /**
     *  myAppName
     */
    actual open val host: String?
        get() = TODO("Not yet implemented")
}