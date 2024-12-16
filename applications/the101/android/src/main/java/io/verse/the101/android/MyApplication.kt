package io.verse.the101.android;

import android.net.Uri
import io.tagd.android.app.TagdApplication
import io.tagd.arch.control.ApplicationInjector

class MyApplication: TagdApplication() {

    override fun newInjector(): ApplicationInjector<out TagdApplication> {
        return MyApplicationInjector(this)
    }

    override fun onDeeplink(uri: Uri) {
        super.onDeeplink(uri)
    }
}
