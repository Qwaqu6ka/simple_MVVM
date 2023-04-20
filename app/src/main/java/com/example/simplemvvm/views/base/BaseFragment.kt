package com.example.simplemvvm.views.base

import androidx.fragment.app.Fragment
import com.example.simplemvvm.MainActivity

abstract class BaseFragment : Fragment() {

    abstract val viewModel: BaseViewModel

    /**
     * Call this method when activity controls (e.g. toolbar) should be re-rendered
     */
    fun notifyScreenUpdates() {
        // if you have more than 1 activity -> you should use a separate interface instead of direct
        // cast to MainActivity
        (requireActivity() as MainActivity).notifyScreenUpdates()
    }
}