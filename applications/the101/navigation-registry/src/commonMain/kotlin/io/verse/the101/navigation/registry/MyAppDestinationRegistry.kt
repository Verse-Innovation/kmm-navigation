package io.verse.the101.navigation.registry

import io.verse.deeplink.core.model.DeepLinkConfig
import io.verse.navigation.core.Destination
import io.verse.navigation.core.DestinationRegistry
import io.verse.navigation.core.DestinationLister

expect class MyAppDestinationRegistry(config: DeepLinkConfig) : DestinationRegistry {

    object SplashServices {

        val PATH_TO_SPLASH: String
    }

    object M1Destinations : DestinationLister {

        val PATH_TO_M1_HOME: String
        val PATH_TO_M1_HOME_FRAGMENT: String
    }

    object M2Destinations : DestinationLister {

        val PATH_TO_M2_SETTINGS: Destination
        val PATH_TO_M2_SETTINGS_FRAGMENT_1: String
        val PATH_TO_M2_SETTINGS_FRAGMENT_2: Destination
        val PATH_TO_M2_SETTINGS_FRAGMENT_2_FRAGMENT_3: Destination
        val PATH_TO_M2_SETTINGS_FRAGMENT_2_FRAGMENT_4: String
    }

    object M3Destinations : DestinationLister {

        val PATH_TO_M3_A: String
    }
}