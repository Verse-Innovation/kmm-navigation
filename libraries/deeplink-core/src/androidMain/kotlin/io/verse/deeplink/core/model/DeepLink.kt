@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.deeplink.core.model

import android.net.Uri
import io.tagd.langx.time.Millis
import io.tagd.langx.time.UnixEpochInMillis
import io.verse.architectures.soa.io.ServiceDataObject
import java.lang.StringBuilder
import java.util.regex.Pattern

/**
 * todo: To support deeplinks with and without hostname, check if appName is there in deeplink
 * config replace add it to the start of deeplink if it isn't there already
 */
actual open class DeepLink actual constructor(
    url: String,
    minSupportedVersion: Int
) : ServiceDataObject(), Comparable<DeepLink> {

    override fun handleAvailable(): Boolean {
        return handle.isNullOrBlank().not()
    }

    actual open val minimumSupportedVersion: Int = minSupportedVersion

    private val uri = Uri.parse(url)

    actual open val url: String = uri.toString()

    /**
     * {scheme}://host/authority/path1/path2
     */
    actual open val scheme: String?
        get() = uri.scheme

    /**
     * scheme://host/{authority}/path1/path2
     */
    actual open val authority: String?
        get() = uri.path?.removePrefix("/")?.split('/')?.first()

    /**
     * scheme://host/authority{/path1/path2}
     */
    actual open val path: String?
        get() = StringBuilder("/")
            .append(resolvePath())
            .toString()

    private fun resolvePath(): String {
        return uri.path?.let { path ->
            val split = path.removePrefix("/").split(Pattern.compile("/"), 2)

            return if (split.size < 2) { // got moudle only in path
                //	fallback to default route
                "default"
            } else {
                split[1]
            }
        } ?: kotlin.run {
            ""
        }
    }

    actual open val segments: List<String>
        get() = uri.pathSegments

    actual open val host: String?
        get() = uri.host

    actual open val queryParameters: Map<String, String?> by lazy {
        val map = hashMapOf<String, String?>()
        uri.queryParameterNames?.forEach { name ->
            map[name] = uri.getQueryParameter(name)
        }
        map
    }

    actual open var interactionTime: UnixEpochInMillis = UnixEpochInMillis(Millis(0L))
        set(value) {
            if (field.millisSince1970.millis != 0L) {
                throw IllegalAccessException("interactionTime can be set only once")
            }
            field = value
        }

    actual open var rank: Int = 0
        set(value) {
            if (field != 0) {
                throw IllegalAccessException("rank can be set only once")
            }
            field = value
        }

    actual override var handle: String = ""
        get() = authority!!
        set(value) {
            field = authority!!
        }

    override fun compareTo(other: DeepLink): Int {
        return rank - other.rank
    }

}