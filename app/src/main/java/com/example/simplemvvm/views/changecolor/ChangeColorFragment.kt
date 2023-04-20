package com.example.simplemvvm.views.changecolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import com.example.simplemvvm.R
import com.example.simplemvvm.databinding.FragmentChangeColorBinding
import com.example.simplemvvm.views.HasCustomTitle
import com.example.simplemvvm.views.base.BaseFragment
import com.example.simplemvvm.views.base.BaseScreen
import com.example.simplemvvm.views.base.screenViewModel

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

        viewModel.colorsList.observe(viewLifecycleOwner) {
            adapter.items = it
        }

        viewModel.screenTitle.observe(viewLifecycleOwner) {
            notifyScreenUpdates()
        }

        binding.cancelButton.setOnClickListener { viewModel.onCancelPressed() }
        binding.saveButton.setOnClickListener { viewModel.onSavePressed() }

        return  binding.root
    }

    private fun setupLayoutManager() {
        binding.colorsRecyclerView.viewTreeObserver.addOnGlobalLayoutListener (object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.colorsRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = binding.colorsRecyclerView.width
                val itemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
                val columns = width / itemWidth
                binding.colorsRecyclerView.layoutManager = GridLayoutManager(requireContext(), columns)
            }
        })
    }
}