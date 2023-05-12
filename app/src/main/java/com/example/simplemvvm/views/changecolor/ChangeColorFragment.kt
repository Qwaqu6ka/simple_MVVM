package com.example.simplemvvm.views.changecolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foundation.views.BaseFragment
import com.example.foundation.views.BaseScreen
import com.example.foundation.views.HasCustomTitle
import com.example.foundation.views.screenViewModel
import com.example.simplemvvm.R
import com.example.simplemvvm.databinding.FragmentChangeColorBinding
import com.example.simplemvvm.views.collectFlow
import com.example.simplemvvm.views.onTryAgain
import com.example.simplemvvm.views.renderSimpleResult

class ChangeColorFragment : BaseFragment(), HasCustomTitle {

    class Screen(
        val currentColorId: Long
    ) : BaseScreen

    override val viewModel by screenViewModel<ChangeColorViewModel>()
    private lateinit var binding: FragmentChangeColorBinding

    override fun getCustomTitle(): String? = viewModel.screenTitle.value

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeColorBinding.inflate(layoutInflater, container, false)

        val adapter = ColorsAdapter(viewModel)
        binding.colorsRecyclerView.adapter = adapter
        setupLayoutManager()

        collectFlow(viewModel.viewState) { result ->
            renderSimpleResult(binding.root, result) { viewState ->
                adapter.items = viewState.colorsList
                binding.saveButton.visibility =
                    if (viewState.showSaveButton) View.VISIBLE else View.INVISIBLE
                binding.cancelButton.visibility =
                    if (viewState.showCancelButton) View.VISIBLE else View.INVISIBLE

                binding.saveProgressGroup.visibility =
                    if (viewState.showSaveProgressBar) View.VISIBLE else View.GONE
                binding.saveProgressBar.progress = viewState.saveProgressPercentage
                binding.savingPercentageTextView.text = viewState.saveProgressPercentageMessage
            }
        }

        viewModel.screenTitle.observe(viewLifecycleOwner) {
            notifyScreenUpdates()
        }

        binding.cancelButton.setOnClickListener { viewModel.onCancelPressed() }
        binding.saveButton.setOnClickListener { viewModel.onSavePressed() }
        onTryAgain(binding.root) {
            viewModel.onTryAgain()
        }

        return binding.root
    }

    private fun setupLayoutManager() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = binding.root.width
                val itemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
                val columns = width / itemWidth
                binding.colorsRecyclerView.layoutManager =
                    GridLayoutManager(requireContext(), columns)
            }
        })
    }
}