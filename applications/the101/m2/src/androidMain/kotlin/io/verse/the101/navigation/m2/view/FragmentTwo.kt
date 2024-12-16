package io.verse.the101.navigation.m2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.verse.deeplink.android.m2.databinding.FragmentTwoBinding
import io.verse.navigation.android.DestinationFragment
import io.verse.navigation.core.DestinationComponent
import kotlin.reflect.KClass

class FragmentTwo : DestinationFragment(), NestedPageTwo {

    private var _binding: FragmentTwoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTwoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override val myPath: String
        get() = MY_PATH

    override fun populateInternalDestinationGraph(
        graph: HashMap<KClass<out DestinationComponent>, Pair<Int, String?>>
    ) {

        graph[FragmentThree::class] = binding.childFragmentContainerView.id to null
        graph[FragmentFour::class] = binding.childFragmentContainerView.id to null
    }

    override fun onReachedFinalDestination() {
        presentDefault()
    }

    private fun presentDefault() {
        setupFragmentTwo()
    }

    private fun setupFragmentTwo() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.add(binding.childFragmentContainerView.id, FragmentThree())
        fragmentTransaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val MY_PATH = "/fragment2"
    }
}