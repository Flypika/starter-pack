package com.flypika.pack.presentation.base.fragment

/**
 * Implement it in your fragment to handle onBackPressed
 * callback in your fragment.
 */
interface OnBackListener {
    /**
     * @return true if if you don't want activity calls super.onBackPressed().
     * Otherwise false.
     */
    fun onBackPressed(): Boolean = false
}