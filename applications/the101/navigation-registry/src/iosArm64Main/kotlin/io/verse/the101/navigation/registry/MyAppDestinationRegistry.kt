package io.verse.the101.navigation.registry

import io.verse.deeplink.core.model.DeepLinkConfig
import io.verse.navigation.core.Destination
import io.verse.navigation.core.DestinationRegistry
import io.verse.navigation.core.DestinationLister

actual class MyAppDestinationRegistry actual constructor(config: DeepLinkConfig) :
    DestinationRegistry(config) {

    actual object SplashServices {
        actual val PATH_TO_SPLASH: String
            get() = TODO("Not yet implemented")
    }

    actual object M1Destinations : DestinationLister() {
        actual val PATH_TO_M1_HOME: String
            get() = TODO("Not yet implemented")
        actual val PATH_TO_M1_HOME_FRAGMENT: String
            get() = TODO("Not yet implemented")
    }

    actual object M2Destinations : DestinationLister() {
        actual val PATH_TO_M2_SETTINGS: Destination
            get() = TODO("Not yet implemented")
        actual val PATH_TO_M2_SETTINGS_FRAGMENT_1: String
            get() = TODO("Not yet implemented")
        actual val PATH_TO_M2_SETTINGS_FRAGMENT_2: Destination
            get() = TODO("Not yet implemented")
        actual val PATH_TO_M2_SETTINGS_FRAGMENT_2_FRAGMENT_3: Destination
            get() = TODO("Not yet implemented")
        actual val PATH_TO_M2_SETTINGS_FRAGMENT_2_FRAGMENT_4: String
            get() = TODO("Not yet implemented")
    }

    actual object M3Destinations : DestinationLister() {
        actual val PATH_TO_M3_A: String
            get() = TODO("Not yet implemented")
    }
}