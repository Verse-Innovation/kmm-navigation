@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.navigation.core

import android.app.Activity
import android.content.Intent
import android.net.Uri
import io.tagd.arch.access.library
import io.tagd.arch.present.mvnp.Navigatable
import io.tagd.arch.present.mvnp.Navigator
import io.tagd.langx.Context
import io.tagd.langx.ref.WeakReference
import io.verse.deeplink.core.handler.DefaultDeepLinkHandler
import io.verse.deeplink.core.model.DeepLink
import kotlin.reflect.KClass

actual abstract class AbstractDeepLinkNavigator<N : Navigatable> actual constructor(
    private val weakContext: WeakReference<Context>,
    handle: String
) : DefaultDeepLinkHandler(handle), Navigator<N> {

    protected actual var registry: DestinationRegistry?
    actual override var navigatable: N? = null

    init {
        val library = library<Navigation>()
        registry = library?.registry
        register()
    }

    private fun register() {
        val outPaths = arrayListOf<String>()
        registerDestinations(outPaths)
        registerDestinationBuilders(*(outPaths.toTypedArray()))
    }

    protected open fun registerDestinations(outPaths: ArrayList<String>) {
        //no-op
    }

    protected open fun registerDestinationBuilders(vararg paths: String) {
        super.register(
            paths = paths,
            builder = { link, result ->
                val destinations = DeepLinkToIntentConverter(weakContext, registry!!).convert(link)
                result.invoke(destinations)
            }
        )
    }

    protected open fun destinationOf(pathAsAction: String, vararg classes: KClass<*>): Destination {
        return destinationOf(handle, pathAsAction, *classes)
    }

    protected open fun ArrayList<String>.include(path: String): String {
        add(path)
        return path
    }

    open fun navigateToAction(
        action: String,
        resolved: ((Intent) -> Unit)? = null,
        failure: ((Throwable) -> Unit)? = null,
        vararg args: Pair<String, Any?>
    ) {

        registry?.destination(action)?.let { destination ->
            intent(destination, resolved, failure, args)
        } ?: kotlin.run {
            navigateToPath(path = action)
        }
    }

    private fun intent(
        destination: Destination,
        resolved: ((Intent) -> Unit)? = null,
        failure: ((Throwable) -> Unit)? = null,
        args: Array<out Pair<String, Any?>>
    ) {

        registry?.intent(destination, *args)?.run {
            weakContext.get()?.let { context ->
                addActivityNewTaskFlagIfRequired(context)
                resolved?.invoke(this)
                context.startActivity(this)
            }
        } ?: kotlin.run {
            failure?.invoke(Throwable("intent not resolved"))
        }
    }

    open fun navigateToPath(
        path: String,
        resolved: ((Intent) -> Unit)? = null,
        failure: ((Throwable) -> Unit)? = null,
        vararg args: Pair<String, Any?>
    ) {

        registry?.let { registry ->
            registry.deepLink(path, *args)?.intent()?.run {
                resolved?.invoke(this)
                weakContext.get()?.let { context ->
                    addActivityNewTaskFlagIfRequired(context)
                    weakContext.get()?.startActivity(this)
                }
            } ?: kotlin.run {
                failure?.invoke(Throwable("destination not resolved"))
            }
        }
    }

    private fun Intent.addActivityNewTaskFlagIfRequired(context: Context) {
        if (context !is Activity) {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    private fun DeepLink.intent(): Intent {
        return url.run {
            Intent(
                Intent.ACTION_DEFAULT,
                Uri.parse(this)
            ).apply {
            }
        }
    }

    actual override fun release() {
        navigatable = null
        registry = null
    }
}