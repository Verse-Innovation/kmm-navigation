package io.verse.the101.navigation.m3.view

import android.os.Bundle
import io.tagd.arch.access.module
import io.tagd.langx.Context
import io.tagd.langx.Intent
import io.verse.deeplink.android.m3.databinding.ActivityBBinding
import io.verse.navigation.android.DestinationActivity
import io.verse.the101.navigation.m3.M3Module

class ActivityB : DestinationActivity() {

    private lateinit var binding: ActivityBBinding
    private var navigator = module<M3Module>()?.navigator?.internalNavigator
    private var moduleNavigator = module<M3Module>()?.navigator
    private var randomArgOne: String? = null
    private var randomArgTwo: Int = -1

    override fun onCreateView(savedInstanceState: Bundle?) {
        binding = ActivityBBinding.inflate(layoutInflater)
        setContentView(binding.root)

        processIntent()
        setClickListeners()
        updateView()
    }

    override val myPath: String
        get() = MY_PATH

    private fun processIntent() {
        randomArgOne = intent.getStringExtra(ARG_RANDOM_ONE)
        randomArgTwo = intent.getIntExtra(ARG_RANDOM_TWO, -1)
    }

    private fun updateView() {
        binding.tvRandArgOne.text = "$randomArgOne"
        binding.tvRandArgTwo.text = "$randomArgTwo"
    }

    private fun setClickListeners() {
        binding.ctaToA.setOnClickListener {
            navigator?.navigateToA(this, "Navigated to A")
        }
        binding.ctaToC.setOnClickListener {
            moduleNavigator?.navigateTo(this, listOf(
                "io.verse.the101.navigation.m3.view.c.ActivityC",
                "io.verse.the101.navigation.m3.view.c.fragment.FragmentWithChildren",
                "io.verse.the101.navigation.m3.view.c.fragment.ChildFragmentOne"
            ))
        }
        binding.ctaToCRoot2.setOnClickListener {
            moduleNavigator?.navigateTo(this, listOf(
                "io.verse.the101.navigation.m3.view.c.ActivityC",
                "io.verse.the101.navigation.m3.view.c.fragment.FragmentRootTwo"
            ))
        }
    }

    companion object {

        const val MY_PATH = "/b"

        private const val ARG_RANDOM_ONE = "key_random_arg"
        private const val ARG_RANDOM_TWO = "key_random_arg_two"

        fun bWithArgOne(context: Context, randomArgOne: String): Intent {
            return Intent(context, ActivityB::class.java).apply {
                putExtra(ARG_RANDOM_ONE, randomArgOne)
            }
        }

        fun bWithArgTwo(context: Context, arg: String, randomArgTwo: Int): Intent {
            return Intent(context, ActivityB::class.java).apply {
                putExtra(ARG_RANDOM_ONE, arg)
                putExtra(ARG_RANDOM_TWO, randomArgTwo)
            }
        }
    }
}