package io.verse.the101.navigation.m3.view

import android.os.Bundle
import io.tagd.arch.access.module
import io.tagd.langx.Context
import io.tagd.langx.Intent
import io.verse.deeplink.android.m3.databinding.ActivityABinding
import io.verse.navigation.android.DestinationActivity
import io.verse.the101.navigation.m3.M3Module

class ActivityA : DestinationActivity() {

    private lateinit var binding: ActivityABinding
    private var randomArgOne: String? = null
    private var navigator = module<M3Module>()?.navigator?.internalNavigator

    override val myPath: String
        get() = MY_PATH

    override fun onCreateView(savedInstanceState: Bundle?) {
        binding = ActivityABinding.inflate(layoutInflater)
        setContentView(binding.root)

        processIntent()
        setClickListeners()
        updateView()
    }

    private fun processIntent() {
        randomArgOne = intent.getStringExtra(ARG_RANDOM)
    }

    private fun setClickListeners() {
        binding.ctaToB.setOnClickListener {
            navigator?.navigateToB(this, "Navigated to B by Way 1")
        }
        binding.ctaToBTwo.setOnClickListener {
            navigator?.navigateToBTwo(this, "Navigated to B by Way 2", 2)
        }
    }

    private fun updateView() {
        binding.tvRandArgOne.text = "$randomArgOne"
    }

    companion object {

        const val MY_PATH = "/a"

        private const val ARG_RANDOM = "key_random_arg"

        fun aWithArgOne(context: Context, randomArgOne: String): Intent {
            return Intent(context, ActivityA::class.java).apply {
                putExtra(ARG_RANDOM, randomArgOne)
            }
        }
    }
}