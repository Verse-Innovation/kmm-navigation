@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.deeplink.core.model

import io.tagd.langx.time.UnixEpochInMillis
import io.verse.architectures.soa.io.ServiceDataObject

/**
 *  A deeplink with the format -- myapp://myAppName/myModuleA/feature1/flow?param=1&param=2
 *
 *  - Scheme
 * 	- Host
 * 		- Authority
 * 			- Path Segements
 * 				- Query Parameters
 *
 * - verse://
 * 	- publicvibe 				| josh | dailyhunt |... <-- The app name (host)
 * 		- home 					| splash | onboarding | vdp |... <-- The module name (authority)
 * 			- tab1				| nearby | following |... <-- The feature name (path segment(s))
 * 				- args?....		| args are query parameters
 * 			- tabN
 * 				- args?....
 * 			- bnb
 * 				- args?....
 * 			- tb
 * 				- args?....
 * 			- menu
 * 				- args?....
 * 				- settings
 * 					- args?....
 * 					- notification
 * 						- args?....
 *
 *
 * scheme://host/authority/path1/path2.../pathN?argA=$&arbB=$....&argX=$
 * scheme://host/authority/path1/path2.../pathN?argA=a&arbB=b....&argX=x
 */
expect open class DeepLink constructor(
    url: String,
    minSupportedVersion: Int
) : ServiceDataObject, Comparable<DeepLink> {

    /**
     * The deeplink may prefer a minimum app version to perform effectively. Otherwise the app would
     * be in erroneous flow
     */
    open val minimumSupportedVersion: Int

    /**
     *  A deeplink with the format -- myapp://myModuleA/feature1/flow?param=1&param=2
     */
    open val url: String

    /**
     *  myapp://
     */
    open val scheme: String?

    /**
     *  myAppName
     */
    open val host: String?

    /**
     *  myModuleA
     */
    open val authority: String?

    /**
     * feature1/flow
     */
    open val path: String?

    /**
     *  [ feature1, flow ]
     */
    open val segments: List<String>

    /**
     * { param1 to 1, param2 to 2 }
     */
    open val queryParameters: Map<String, String?>

    /**
     * the time at which deep link is clicked/touched/interacted
     */
    open var interactionTime: UnixEpochInMillis

    /**
     * Let's say there are more than one deeplinks resolved from deferred deeplink mechanisms,
     * then there must be a way which one to consider. The rank is purely business specific
     * - example scenario - prefer facebook deferred deep link over firebase one etc
     */
    open var rank: Int

    override var handle: String
}

open class DeferredDeepLink(
    url: String,
    minSupportedVersionCode: Int,
    val derivedAttribution: Map<String, String> = hashMapOf(),
    val autoFetchedPayload: String? = null,
) : DeepLink(url = url, minSupportedVersion = minSupportedVersionCode) {

    val contentDeepLink: DeepLink? by lazy {
        null //todo
    }
}