package com.example.foundation.views

/**
 * If your fragment wants to show custom screen title in the toolbar, implement this
 * interface and override [getCustomTitle] method.
 *
 * Please note if screen title can be changed dynamically while fragment is active, you should
 * call [BaseFragment.notifyScreenUpdates] method to re-render activity toolbar.
 */
interface HasCustomTitle {

    fun getCustomTitle(): String?
}