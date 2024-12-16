package io.verse.navigation.android

import android.os.Bundle
import androidx.core.os.bundleOf
import io.tagd.android.mvnp.NavigatableActivity
import io.tagd.arch.present.mvnp.NavigatablePresenter
import io.tagd.arch.present.mvnp.NavigatableView
import io.tagd.arch.present.mvnp.Navigator
import io.verse.navigation.core.Destination
import io.verse.navigation.core.DestinationComponent
import kotlin.reflect.KClass

abstract class NavigatableDestinationActivity<
    V : NavigatableView,
    N : Navigator<V>,
    P : NavigatablePresenter<V, N>
> : NavigatableActivity<V, N, P>(), DestinationComponent {

    final override val internalGraph =
        hashMapOf<KClass<out DestinationComponent>, Pair<Int, String?>>()

    @Suppress("LeakingThis")
    protected open var destinationResolver: ActivityAwareDestinationResolver? =
        ActivityAwareDestinationResolver(this, myPath, internalGraph)

    override fun onPostCreate(savedInstanceState: Bundle?) {
        populateInternalDestinationGraph(internalGraph) // can be loaded from xml/manifest
        destinationResolver?.continueDestinationResolution {
            onReachedFinalDestination()
        }
        super.onPostCreate(savedInstanceState)
    }

    fun navigateInternal(
        componentChain: List<Class <out DestinationComponent>> = listOf(),
        vararg args: Pair<String, Any>
    ) {

        intent.putStringArrayListExtra(
            Destination.REMAINING_COMPONENTS,
            arrayListOf<String>().apply {
                addAll(componentChain.map { it.canonicalName } as Collection<String>)
            }
        )
        intent.putExtra(Destination.ARGUMENTS, bundleOf(*args))

        destinationResolver?.continueDestinationResolution {
            //no-op
        }
    }
}

