package io.verse.the101.navigation.m2.view

import android.os.Bundle
import io.verse.deeplink.android.m2.databinding.ActivitySettingsBinding
import io.verse.navigation.android.DestinationActivity
import io.verse.navigation.core.DestinationComponent
import io.verse.the101.navigation.registry.MyAppDestinationRegistry.M2Destinations
import kotlin.reflect.KClass

class SettingsActivity : DestinationActivity(), SettingsPage {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreateView(savedInstanceState: Bundle?) {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override val myPath: String
        get() = M2Destinations.PATH_TO_M2_SETTINGS.pathAsAction

    override fun populateInternalDestinationGraph(
        graph: HashMap<KClass<out DestinationComponent>, Pair<Int, String?>>
    ) {

        graph[FragmentOne::class] = binding.fragmentHolder.id to "one"
        graph[FragmentTwo::class] = binding.fragmentHolder.id to "two"
    }

    override fun onReachedFinalDestination() {
        presentDefault()
    }

    private fun presentDefault() {
        setupSettingsFragment()
    }

    private fun setupSettingsFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentHolder.id, FragmentOne())
        fragmentTransaction.commit()
    }
}