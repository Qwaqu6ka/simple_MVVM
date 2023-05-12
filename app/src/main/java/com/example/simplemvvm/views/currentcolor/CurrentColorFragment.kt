package com.example.simplemvvm.views.currentcolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foundation.views.BaseFragment
import com.example.foundation.views.BaseScreen
import com.example.foundation.views.screenViewModel
import com.example.simplemvvm.databinding.FragmentCurrentColorBinding
import com.example.simplemvvm.views.onTryAgain
import com.example.simplemvvm.views.renderSimpleResult

class CurrentColorFragment : BaseFragment() {

    // no arguments for this screen
    class Screen : BaseScreen

    override val viewModel by screenViewModel<CurrentColorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCurrentColorBinding.inflate(layoutInflater, container, false)

        viewModel.currentColor.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onSuccess = {
                    binding.colorView.setBackgroundColor(it.value)
                }
            )
        }

        onTryAgain(binding.root) {
            viewModel.onTryAgain()
        }

        binding.changeColorButton.setOnClickListener { viewModel.changeColor() }

        return binding.root
    }
}