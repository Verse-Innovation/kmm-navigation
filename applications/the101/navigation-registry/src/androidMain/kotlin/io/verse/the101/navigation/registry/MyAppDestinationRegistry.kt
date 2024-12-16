package io.verse.the101.navigation.registry

import io.verse.deeplink.core.model.DeepLinkConfig
import io.verse.navigation.core.Destination
import io.verse.navigation.core.DestinationRegistry
import io.verse.navigation.core.DestinationLister
import io.verse.navigation.core.destinationOf

actual class MyAppDestinationRegistry actual constructor(config: DeepLinkConfig) :
    DestinationRegistry(config) {

    actual object SplashServices {

        actual const val PATH_TO_SPLASH = "/splash"
    }

    actual object M1Destinations : DestinationLister() {

        override val DEFAULT_PATH: String = "/default"
        actual const val PATH_TO_M1_HOME = "/home"
        actual const val PATH_TO_M1_HOME_FRAGMENT = "/home/fragmentHome"
    }

    actual object M2Destinations : DestinationLister() {

        actual val PATH_TO_M2_SETTINGS: Destination = destinationOf(
            authority = "m2",
            pathAsAction = "/settings",
            "io.verse.the101.navigation.m2.view.SettingsActivity"
        )

        actual const val PATH_TO_M2_SETTINGS_FRAGMENT_1: String = "/settings/fragment1"

        actual val PATH_TO_M2_SETTINGS_FRAGMENT_2: Destination = Destination(
            authority = "m2",
            pathAsAction = "/settings/fragment2",
            components = arrayListOf(
                "io.verse.the101.navigation.m2.view.SettingsActivity",
                "io.verse.the101.navigation.m2.view.FragmentTwo"
            )
        )

        actual val PATH_TO_M2_SETTINGS_FRAGMENT_2_FRAGMENT_3: Destination = destinationOf(
            authority = "m2",
            pathAsAction = "/settings/fragment2/fragment3",
            "io.verse.the101.navigation.m2.view.SettingsActivity",
            "io.verse.the101.navigation.m2.view.FragmentTwo",
            "io.verse.the101.navigation.m2.view.FragmentThree"
        )

        actual const val PATH_TO_M2_SETTINGS_FRAGMENT_2_FRAGMENT_4: String =
            "/settings/fragment2/fragment4"
    }

    actual object M3Destinations : DestinationLister() {

        actual const val PATH_TO_M3_A = "/a"
    }
}