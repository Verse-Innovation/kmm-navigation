package io.verse.navigation.core

import io.tagd.langx.Callback
import kotlin.reflect.KClass

interface DestinationComponent {

    val internalGraph: HashMap<KClass<out DestinationComponent>, Pair<Int, String?>>

    val myPath: String

    fun populateInternalDestinationGraph(
        graph: HashMap<KClass<out DestinationComponent>, Pair<Int, String?>>
    ) {
        //no-op
    }

    fun onReachedFinalDestination() {
        //no-op
    }
}

interface DestinationResolver : DestinationComponent {

    override fun populateInternalDestinationGraph(
        graph: HashMap<KClass<out DestinationComponent>, Pair<Int, String?>>
    ) {

        throw UnsupportedOperationException("please rely on the caller")
    }

    override fun onReachedFinalDestination() {
        throw UnsupportedOperationException("please rely on the caller")
    }

    fun continueDestinationResolution(fallback: Callback<Unit>? = null)

    fun hasRemainingPath(): Boolean

    fun remainingPath(): String?

    fun remainingComponents(): ArrayList<String>?

    fun nextComponentClassName(): String?

    fun nextComponent(componentClassName: String): DestinationComponent?

    fun nextComponentRemainingPath(): String?

    fun nextComponentRemainingComponents(): ArrayList<String>?

    fun navigateToNextComponent(component: DestinationComponent)
}