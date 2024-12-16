package io.verse.the101.navigation.m1.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import io.tagd.arch.access.library
import io.verse.deeplink.android.m1.databinding.ActivityHomeBinding
import io.verse.the101.navigation.registry.MyAppDestinationRegistry.M2Destinations
import io.verse.navigation.android.DestinationActivity
import io.verse.navigation.core.DestinationComponent
import io.verse.navigation.core.Navigation
import kotlin.reflect.KClass

class HomeActivity : DestinationActivity(), HomePage {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreateView(savedInstanceState: Bundle?) {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setView()
        setClickListeners()
    }

    override val myPath: String
        get() = MY_PATH

    override fun populateInternalDestinationGraph(
        graph: HashMap<KClass<out DestinationComponent>, Pair<Int, String?>>
    ) {

        graph[FragmentHome::class] = binding.fragmentHolder.id to null
    }

    override fun onReachedFinalDestination() {
        presentDefault()
    }

    private fun setView() {
        binding.tvHome.text = "Home"
    }

    private fun setClickListeners() {
        val pathRegistry = library<Navigation>()?.registry

        binding.btnSettings.setOnClickListener {
            val deepLink =
                pathRegistry?.deepLink(
                    M2Destinations.PATH_TO_M2_SETTINGS,
                    "arg1" to 2,
                    "arg2" to 1
                )
            deepLink?.url?.let {
                Intent(Intent.ACTION_DEFAULT, Uri.parse(it)).run {
                    startActivity(this)
                }
            }

        }
        binding.btnSettings2.setOnClickListener {
            val deepLink =
                pathRegistry?.deepLink(
                    M2Destinations.PATH_TO_M2_SETTINGS_FRAGMENT_2,
                    "arg1" to 2,
                    "arg2" to 1
                )
            deepLink?.url?.let {
                Intent(Intent.ACTION_DEFAULT, Uri.parse(it)).run {
                    startActivity(this)
                }
            }

        }
        binding.btnSettingsFrag2Frag3.setOnClickListener {
            val deepLink =
                pathRegistry?.deepLink(
                    M2Destinations.PATH_TO_M2_SETTINGS_FRAGMENT_2_FRAGMENT_3,
                    "arg1" to 2,
                    "arg2" to 1
                )
            deepLink?.url?.let {
                Intent(Intent.ACTION_DEFAULT, Uri.parse(it)).run {
                    startActivity(this)
                }
            }

        }
        binding.btnSettingsFrag2Frag4.setOnClickListener {
            pathRegistry?.destination(
                M2Destinations.PATH_TO_M2_SETTINGS_FRAGMENT_2_FRAGMENT_4
            )?.let {
                val deepLink = pathRegistry.deepLink(
                    it,
                    "arg1" to 2,
                    "arg2" to 1
                )
                deepLink.url.let {
                    Intent(Intent.ACTION_DEFAULT, Uri.parse(it)).run {
                        startActivity(this)
                    }
                }
            }
        }

        binding.btnSettingsFrag1.setOnClickListener {
            pathRegistry?.destination(M2Destinations.PATH_TO_M2_SETTINGS_FRAGMENT_1)?.let {
                pathRegistry.intent(it).run {
                    startActivity(this)
                }
            }
        }

        binding.btnSettingsFrag2Frag4Action.setOnClickListener {
            pathRegistry?.destination(
                M2Destinations.PATH_TO_M2_SETTINGS_FRAGMENT_2_FRAGMENT_4
            )?.let {
                pathRegistry.intent(it).run {
                    startActivity(this)
                }
            }
        }
    }

    private fun presentDefault() {
        // setupHomeFragment()
    }

    private fun setupHomeFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(binding.fragmentHolder.id, FragmentHome())
        fragmentTransaction.commit()
    }

    companion object {

        const val MY_PATH = "/home"

        private const val ARG_RANDOM = "key_random_arg"

        fun createNewInstanceBundle(randomArg: String): Bundle {
            val args = Bundle()
            args.putString(ARG_RANDOM, randomArg)
            return args
        }
    }
}