package io.verse.navigation.android

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import io.tagd.arch.access.library
import io.tagd.langx.Callback
import io.tagd.core.Initializable
import io.tagd.core.Releasable
import io.verse.navigation.core.Destination
import io.verse.navigation.core.Navigation
import java.lang.ref.WeakReference

abstract class DestinationNavigator(
    private var weakActivityResultRegistry: WeakReference<ActivityResultRegistry>
): Initializable, Releasable {

    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null
    private var activityResult: Callback<ActivityResult>? = null

    override fun initialize() {
        registerForActivityResult()
    }

    private fun registerForActivityResult() {
        activityResultLauncher = weakActivityResultRegistry.get()?.register(
            activityResultIdentifierKey(),
            StartActivityForResult()
        ) { result ->
            activityResult?.invoke(result)
        }
    }

    abstract fun activityResultIdentifierKey(): String

    fun launchForResult(
        destinationPath: String,
        destinationResolved: ((Intent) -> Unit)? = null,
        result: Callback<ActivityResult>,
        vararg args: Pair<String, Any?>
    ) {

        val destinationRegistry = library<Navigation>()?.registry
        destinationRegistry
            ?.destination(destinationPath)
            ?.launchForResult(destinationResolved, result, *args)
    }

    fun Destination.launchForResult(
        destinationResolved: ((Intent) -> Unit)?,
        result: Callback<ActivityResult>,
        vararg args: Pair<String, Any?>
    ) {

        val destinationRegistry = library<Navigation>()?.registry
        destinationRegistry?.intent(this, *args)?.let { intent ->
            activityResult = result
            activityResultLauncher?.launch(intent)
            destinationResolved?.invoke(intent)
        }
    }

    override fun release() {
        activityResult = null
        activityResultLauncher = null
        weakActivityResultRegistry.clear()
    }
}