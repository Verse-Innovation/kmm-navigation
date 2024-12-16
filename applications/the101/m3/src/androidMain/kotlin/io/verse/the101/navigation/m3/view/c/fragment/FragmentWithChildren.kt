package io.verse.the101.navigation.m3.view.c.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.verse.deeplink.android.m3.R
import io.verse.navigation.android.DestinationFragment
import io.verse.navigation.core.DestinationComponent
import kotlin.reflect.KClass

class FragmentWithChildren : DestinationFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_childrens, container, false)
    }

    override val myPath: String
        get() = MY_PATH

    override fun populateInternalDestinationGraph(
        graph: HashMap<KClass<out DestinationComponent>, Pair<Int, String?>>
    ) {

        graph[ChildFragmentOne::class] = R.id.fragment_holder to "ChildFragmentOne"
        graph[ChildFragmentTwo::class] = R.id.fragment_holder to "ChildFragmentTwo"
        graph[ChildFragmentThree::class] = R.id.fragment_holder to "ChildFragmentThree"
    }

    override fun onReachedFinalDestination() {
        presentDefault()
    }

    private fun presentDefault() {
        setupFragmentTwo()
    }

    private fun setupFragmentTwo() {
        navigateInternal(listOf(ChildFragmentTwo::class.java))
    }

    companion object {

        const val MY_PATH = "/fragmentChildrens"
    }
}