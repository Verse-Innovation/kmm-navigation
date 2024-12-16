package io.verse.navigation.core

import io.tagd.arch.datatype.DataObject
import kotlin.reflect.KClass

/**
 * There must be always a 1 to 1 correspondence a url path to implementation components.
 * If a path is
 *  PATH = my/relative/path/to/component?arg1="arg1Value"&arg2="arg2Value"
 *      then the corresponding component sequence/path/order is
 *  COMPONENTS = MyWindowPage/NestedPage1/NestedPage11/NestedPage111/NestedPage1111
 *
 *  The path to components mapping would be
 *  my          = MyWindowPage
 *  relative    = NestedPage1
 *  path        = NestedPage11
 *  to          = NestedPage111
 *  component   = NestedPage1111
 *
 *  Note - If developer renames a path specific component, then he/she must ensure that,
 *  the change is reflected in component sequence. Since it is a string based, the IDE won't find
 *  and replace (alternatively while refactoring please tick check in comments and usage).
 *
 *  @property pathAsAction
 *      behaves both as a relative path and also as an [io.tagd.langx.Intent]'s action
 */
data class Destination(
    val authority: String,
    val pathAsAction: String,
    val components: List<String> = listOf()
) : DataObject() {

    fun remaining() : Destination? {
        val pathFrom1stSegment = pathAsAction.nextRemainingPath()
        return if (pathFrom1stSegment != null) {
            val componentsFrom1stSegment = if (components.size > 1) {
                components.subList(1, components.size)
            } else {
                listOf()
            }
            Destination(authority, pathFrom1stSegment, componentsFrom1stSegment)
        } else {
            null
        }
    }

    companion object {
        const val REMAINING_PATH = "io.verse.navigation.core.unresolved.path"
        const val REMAINING_COMPONENTS = "io.verse.navigation.core.unresolved.components"
        const val ARGUMENTS = "io.verse.navigation.core.unresolved.args"
    }
}

fun destinationOf(authority: String, pathAsAction: String, vararg classes: KClass<*>): Destination {
    val components: List<String> = classes.map {
        it.qualifiedName!!
    }
    return Destination(authority = authority, pathAsAction = pathAsAction, components = components)
}

fun destinationOf(authority: String, pathAsAction: String, vararg components: String): Destination {
    return Destination(
        authority = authority,
        pathAsAction = pathAsAction,
        components = listOf(*components)
    )
}

/**
 * Input path = /a/b/c/d
 * Outputs path = /b/d/d
 */
fun String.nextRemainingPath(): String? {
    val pathWithLeadingSlash = this
    val secondarySlashIndex = pathWithLeadingSlash.indexOf('/', 1)
    return if (secondarySlashIndex != -1) {
        return pathWithLeadingSlash.substring(secondarySlashIndex)
    } else {
        null
    }
}
