package io.verse.the101.navigation.m3

import android.app.Activity
import io.tagd.android.app.Fragment
import io.tagd.langx.Intent
import io.verse.the101.navigation.m3.view.ActivityA
import io.verse.the101.navigation.m3.view.ActivityB

internal class M3ModuleInternalNavigator(private val scopableName: String) {

    fun navigateToB(context: Activity, arg: String, flags: Int = -1) {
        context.startActivity(ActivityB.bWithArgOne(context, arg).plus(flags))
    }

    fun navigateToBTwo(context: Activity, arg: String, arg2: Int, flags: Int = -1) {
        context.startActivity(ActivityB.bWithArgTwo(context, arg, arg2).plus(flags))
    }

    fun navigateToA(context: Activity, arg: String, flags: Int = -1) {
        context.startActivity(ActivityA.aWithArgOne(context, arg).plus(flags))
    }

    fun intentToC(context: Activity, arg: String, flags: Int = -1) {
        context.startActivity(ActivityA.aWithArgOne(context, arg).plus(flags))
    }

    private fun Intent.plus(flags: Int, moduleName: String = scopableName): Intent {
        apply {
            putExtra("MODULE", moduleName)
            if (flags != -1) {
                addFlags(flags)
            }
        }
        return this
    }
}
