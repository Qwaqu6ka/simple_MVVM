package com.example.foundation.views

import com.example.foundation.ActivityScopeViewModel

interface FragmentHolder {

    fun notifyScreenUpdates()

    fun getActivityScopeViewModel(): ActivityScopeViewModel
}