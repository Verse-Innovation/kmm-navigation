package io.verse.navigation.android

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import io.tagd.android.mvnp.NavigatableFragment
import io.tagd.arch.present.mvnp.NavigatablePresenter
import io.tagd.arch.present.mvnp.NavigatableView
import io.tagd.arch.present.mvnp.Navigator
import io.verse.navigation.core.Destination
import io.verse.navigation.core.DestinationComponent
import kotlin.reflect.KClass

abstract class NavigatableDestinationFragment<
    V : NavigatableView,
    N : Navigator<V>,
    P : NavigatablePresenter<V, N>
> : NavigatableFragment<V, N, P>(), DestinationComponent {

    final override val internalGraph =
        hashMapOf<KClass<out DestinationComponent>, Pair<Int, String?>>()

    @Suppress("LeakingThis")
    protected open var destinationResolver: FragmentAwareDestinationResolver? =
        FragmentAwareDestinationResolver(this, myPath, internalGraph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        populateInternalDestinationGraph(internalGraph) // can be loaded from xml/manifest
        destinationResolver?.continueDestinationResolution {
            onReachedFinalDestination()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    fun navigateInternal(
        componentChain: List<Class <out DestinationComponent>> = listOf(),
        vararg args: Pair<String, Any>
    ) {

        requireArguments().putStringArrayList(
            Destination.REMAINING_COMPONENTS,
            arrayListOf<String>().apply {
                addAll(componentChain.map { it.canonicalName } as Collection<String>)
            }
        )
        requireArguments().putBundle(Destination.ARGUMENTS, bundleOf(*args))

        destinationResolver?.continueDestinationResolution {
            //no-op
        }
    }
}
