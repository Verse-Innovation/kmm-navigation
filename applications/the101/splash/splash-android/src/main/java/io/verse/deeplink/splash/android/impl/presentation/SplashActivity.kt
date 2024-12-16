package io.verse.deeplink.splash.android.impl.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.tagd.arch.access.library
import io.tagd.arch.access.module
import io.verse.deeplink.android.DeferredDeepLinkActivity
import io.verse.deeplink.android.DeferredDeepLinkNavigator
import io.verse.deeplink.core.presentation.IDeferredDeepLinkViewNavigator
import io.verse.deeplink.splash.android.spec.presentation.ISplashView
import io.verse.deeplink.splash.android.spec.presentation.SplashPresenter
import io.verse.deeplink.splash.splash.SplashModule
import io.verse.navigation.core.Navigation
import io.verse.the101.navigation.registry.MyAppDestinationRegistry.M1Destinations
import io.verse.the101.navigation.registry.MyAppDestinationRegistry.M3Destinations
import java.lang.ref.WeakReference

@SuppressLint("CustomSplashScreen")
class SplashActivity : DeferredDeepLinkActivity<
        ISplashView,
        IDeferredDeepLinkViewNavigator<ISplashView>,
    SplashPresenter
>(), ISplashView {

    override fun onCreateNavigator(
        savedInstanceState: Bundle?
    ): IDeferredDeepLinkViewNavigator<ISplashView> {

        return DeferredDeepLinkNavigator<ISplashView>(WeakReference(this)).also {
            navigator = it
        }
    }

    override fun onCreateView(savedInstanceState: Bundle?) {
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.primary
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HomeSimpleWidget()
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HomeFragmentWidget()
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            M3AWidget()
                        }
                    }
                }
            }
        }
    }

    override val myPath: String
        get() = MY_PATH

    @Composable
    private fun HomeSimpleWidget() {
        Text(text = "Home —> ")
        Button(
            content = {
                Icon(
                    Filled.Home,
                    "contentDescription",
                    tint = MaterialTheme.colors.secondary
                )
            },
            onClick = {
                module<SplashModule>()?.navigator?.navigateToPath(
                    M1Destinations.PATH_TO_M1_HOME,
                    resolved = null,
                    failure = null,
                    "arg1" to 2,
                    "arg2" to 1
                )
            })
    }

    @Composable
    private fun HomeFragmentWidget() {
        Text(text = "Home with Fragment —> ")
        Button(
            content = {
                Icon(
                    Filled.KeyboardArrowRight,
                    "contentDescription",
                    tint = MaterialTheme.colors.secondary
                )
            },
            onClick = {
                module<SplashModule>()?.navigator?.navigateToAction(
                    M1Destinations.PATH_TO_M1_HOME_FRAGMENT,
                    resolved = null,
                    failure = null,
                    "arg1" to 2,
                    "arg2" to 1
                )
            }
        )
    }

    @Composable
    private fun M3AWidget() {
        Text(text = "M3 A Activity —> ")
        Button(
            content = {
                Icon(
                    Filled.KeyboardArrowUp,
                    "contentDescription",
                    tint = MaterialTheme.colors.secondary
                )
            },
            onClick = {
                library<Navigation>()?.registry?.let { registry ->
                    registry.destination(M3Destinations.PATH_TO_M3_A)
                        ?.let {
                            val deepLink =
                                registry.deepLink(
                                    it,
                                    "arg1" to 2,
                                    "arg2" to 1
                                )
                            deepLink.url.let { url ->
                                Intent(
                                    Intent.ACTION_DEFAULT,
                                    Uri.parse(url)
                                ).run {
                                    startActivity(this)
                                }
                            }
                        }
                }
            })
    }

    override fun onCreatePresenter(savedInstanceState: Bundle?): SplashPresenter {
        return SplashPresenter(this, navigator!!)
    }

    override fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    companion object {

        const val MY_PATH = "/splash"
    }
}
