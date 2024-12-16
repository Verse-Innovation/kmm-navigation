package io.verse.navigation.android

import android.os.Bundle
import io.tagd.android.app.AppCompatActivity
import io.tagd.android.app.Fragment
import io.tagd.langx.Callback
import io.verse.navigation.core.Destination
import io.verse.navigation.core.DestinationComponent
import io.verse.navigation.core.DestinationResolver
import io.verse.navigation.core.nextRemainingPath
import java.lang.ref.WeakReference
import kotlin.reflect.KClass

open class ActivityAwareDestinationResolver(
    activity: AppCompatActivity,
    override val myPath: String,
    override val internalGraph: HashMap<KClass<out DestinationComponent>, Pair<Int, String?>>
) : DestinationResolver {

    private var weakActivity: WeakReference<out AppCompatActivity>? = WeakReference(activity)

    final override fun continueDestinationResolution(fallback: Callback<Unit>?) {
        nextComponentClassName()?.let { className ->
            nextComponent(className)?.let { fragment ->
                setupFragmentIfItIsDestination(fragment)
                navigateToNextComponent(fragment)
            } ?: kotlin.run {
                fallback?.invoke(Unit)
            }
        } ?: kotlin.run {
            fallback?.invoke(Unit)
        }
    }

    final override fun nextComponentClassName(): String? {
        val unresolvedPathClasses = remainingComponents()
        return if (!unresolvedPathClasses.isNullOrEmpty()) {
            unresolvedPathClasses[0]
        } else null
    }

    override fun nextComponent(componentClassName: String): DestinationFragment? {
        return try {
            Class.forName(componentClassName).newInstance() as DestinationFragment
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun navigateToNextComponent(component: DestinationComponent) {
        if (component is DestinationFragment) {
            val transaction = weakActivity?.get()!!.supportFragmentManager.beginTransaction()
            transaction.add(
                internalGraph[component::class]!!.first,
                component,
                internalGraph[component::class]!!.second
            )
            transaction.commit()
        }
    }

    private fun setupFragmentIfItIsDestination(fragment: DestinationComponent) {
        if (isFragmentDestination(fragment)) {
            fragment as Fragment

            if (fragment.arguments == null) {
                fragment.arguments = Bundle()
            }
            fragment.arguments?.let { args ->
                args.putString(Destination.REMAINING_PATH, nextComponentRemainingPath())
                args.putStringArrayList(
                    Destination.REMAINING_COMPONENTS,
                    nextComponentRemainingComponents()
                )
                args.putBundle(
                    Destination.ARGUMENTS,
                    weakActivity!!.get()!!.intent.getBundleExtra(Destination.ARGUMENTS)
                )
            }
        }
    }

    private fun isFragmentDestination(fragment: DestinationComponent) =
        fragment is DestinationFragment || fragment is NavigatableDestinationFragment<*, *, *>

    final override fun nextComponentRemainingPath(): String? {
        return remainingPath()?.nextRemainingPath()
    }

    final override fun nextComponentRemainingComponents(): ArrayList<String>? {
        return remainingComponents()?.apply { removeAt(0) }
    }

    final override fun remainingComponents() =
        weakActivity?.get()?.intent?.getStringArrayListExtra(Destination.REMAINING_COMPONENTS)

    final override fun hasRemainingPath() = remainingPath().isNullOrBlank().not()

    final override fun remainingPath() =
        weakActivity?.get()?.intent?.getStringExtra(Destination.REMAINING_PATH)
}