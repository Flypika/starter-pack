package com.flypika.pack.cicerone.util

import android.os.Parcelable
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import kotlinx.android.parcel.Parcelize
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace

open class SnapshotNavigator(
    activity: FragmentActivity,
    containerId: Int,
    fragmentManager: FragmentManager = activity.supportFragmentManager
) : SupportAppNavigator(activity, fragmentManager, containerId) {

    interface ParcelableSnapshot : Parcelable

    @Parcelize
    private data class Snapshot(val stack: List<ParcelableScreen>) : ParcelableSnapshot

    private val parcelableStack = ArrayList<ParcelableScreen>()

    fun makeSnapshot(): ParcelableSnapshot = Snapshot(parcelableStack)

    fun restore(snapshot: ParcelableSnapshot) {
        snapshot as Snapshot
        val commands = mutableListOf<Command>()
        commands += BackTo(null)
        if (snapshot.stack.isNotEmpty()) {
            commands += RestoreReplace(snapshot.stack[0])
        }
        commands += with(snapshot.stack) {
            takeLast(size - 1)
        }.map {
            RestoreForward(it)
        }
        applyCommands(commands.toTypedArray())
    }

    override fun fragmentBack() {
        pop()
        super.fragmentBack()
    }

    override fun fragmentReplace(command: Replace) {
        pop()
        push(command.screen.toParcelable())
        super.fragmentReplace(command)
    }

    override fun fragmentForward(command: Forward) {
        push(command.screen.toParcelable())
        super.fragmentForward(command)
    }

    override fun backTo(command: BackTo) {
        parcelableStack.apply {
            repeat(size - indexOf(command.screen) - 1) {
                removeAt(lastIndex)
            }
        }
        super.backTo(command)
    }

    private fun push(forward: ParcelableScreen) {
        parcelableStack += forward
    }

    private fun pop(): ParcelableScreen? {
        val index = parcelableStack.lastIndex
        return if (index >= 0) parcelableStack.removeAt(index) else null
    }

    private fun Screen.toParcelable(): ParcelableScreen = this as ParcelableScreen
}
