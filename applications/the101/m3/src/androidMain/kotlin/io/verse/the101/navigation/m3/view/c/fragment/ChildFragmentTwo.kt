package io.verse.the101.navigation.m3.view.c.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import io.verse.deeplink.android.m3.R
import io.verse.navigation.android.DestinationActivity
import io.verse.navigation.android.DestinationFragment

class ChildFragmentTwo : DestinationFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_child, container, false).apply {
            background = ResourcesCompat.getDrawable(
                context.resources,
                android.R.color.holo_red_light,
                null
            )
        }
    }

    override val myPath: String
        get() = MY_PATH

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.tv_title).text = MY_PATH
        view.findViewById<Button>(R.id.destination_cta).apply {
            text = "Go to Three"
            setOnClickListener {
                (parentFragment as DestinationFragment).navigateInternal(
                    listOf(ChildFragmentThree::class.java)
                )
            }
        }
    }

    companion object {

        const val MY_PATH = "/childFragmentTwo"
    }
}