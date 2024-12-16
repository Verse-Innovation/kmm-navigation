package io.verse.the101.navigation.m2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.verse.deeplink.android.m2.R
import io.verse.navigation.android.DestinationFragment

class FragmentFour : DestinationFragment(), NestedPageFour {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_four, container, false)
    }

    override val myPath: String
        get() = MY_PATH

    companion object {

        const val MY_PATH = "/fragment4"
    }
}