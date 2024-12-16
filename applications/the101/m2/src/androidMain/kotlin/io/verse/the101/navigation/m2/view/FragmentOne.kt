package io.verse.the101.navigation.m2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.verse.deeplink.android.m2.R
import io.verse.navigation.android.DestinationFragment

class FragmentOne : DestinationFragment(), NestedPageOne {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_one, container, false)
    }

    override val myPath: String
        get() = MY_PATH

    companion object {

        const val MY_PATH = "/fragment1"
    }
}