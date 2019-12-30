package com.flypika.pack.ui.util

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

inline fun FragmentManager.transaction(
    commitNow: Boolean = false,
    commitAllowStateLoss: Boolean = false,
    block: FragmentTransaction.() -> Unit
) {
    beginTransaction().apply {
        block()
        if (commitNow) {
            if (commitAllowStateLoss) {
                commitNowAllowingStateLoss()
            } else {
                commitNow()
            }
        } else {
            if (commitAllowStateLoss) {
                commitAllowingStateLoss()
            } else {
                commit()
            }
        }
    }

}
