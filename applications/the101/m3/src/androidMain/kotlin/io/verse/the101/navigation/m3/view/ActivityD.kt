package io.verse.the101.navigation.m3.view

import android.os.Bundle
import io.tagd.arch.access.module
import io.tagd.langx.Context
import io.tagd.langx.Intent
import io.verse.deeplink.android.m3.databinding.ActivityABinding
import io.verse.navigation.android.DestinationActivity
import io.verse.the101.navigation.m3.M3Module

class ActivityD : DestinationActivity() {

    private lateinit var binding: ActivityABinding

    override val myPath: String
        get() = MY_PATH

    override fun onCreateView(savedInstanceState: Bundle?) {
        binding = ActivityABinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvTitle.text = "D"
    }

    companion object {

        const val MY_PATH = "/d"
    }
}