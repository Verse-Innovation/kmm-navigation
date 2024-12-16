package io.verse.the101.navigation.m3.view.c

import android.os.Bundle
import io.tagd.arch.access.module
import io.verse.deeplink.android.m3.R
import io.verse.deeplink.android.m3.databinding.ActivityCBinding
import io.verse.navigation.android.DestinationActivity
import io.verse.navigation.core.DestinationComponent
import io.verse.the101.navigation.m3.M3Module
import io.verse.the101.navigation.m3.view.c.fragment.FragmentRootTwo
import io.verse.the101.navigation.m3.view.c.fragment.FragmentWithChildren
import kotlin.reflect.KClass

class ActivityC : DestinationActivity() {

    private lateinit var binding: ActivityCBinding
    private var navigator = module<M3Module>()?.navigator?.internalNavigator


    override fun onCreateView(savedInstanceState: Bundle?) {
        binding = ActivityCBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClickListeners()
    }

    override val myPath: String
        get() = MY_PATH

    override fun populateInternalDestinationGraph(
        graph: HashMap<KClass<out DestinationComponent>, Pair<Int, String?>>
    ) {

        graph[FragmentWithChildren::class] = R.id.fragment_holder to "FragmentWithChildren"
        graph[FragmentRootTwo::class] = R.id.fragment_holder to "FragmentRootTwo"
    }

    override fun onReachedFinalDestination() {
        processIntent()
    }

    private fun processIntent() {
        navigateInternal(listOf(FragmentWithChildren::class.java))
    }

    private fun setClickListeners() {
        binding.btnCta.setOnClickListener {
            navigateInternal(listOf(FragmentRootTwo::class.java))
        }
    }


    companion object {

        const val MY_PATH = "/c"
    }
}