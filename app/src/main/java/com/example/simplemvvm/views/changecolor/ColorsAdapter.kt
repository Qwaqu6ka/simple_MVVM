package com.example.simplemvvm.views.changecolor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemvvm.databinding.ItemColorBinding
import com.example.simplemvvm.model.colors.NamedColor

class ColorsAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<ColorsAdapter.Holder>(), View.OnClickListener {

    var items: List<NamedColorListItem> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onClick(v: View) {
        val item = v.tag as NamedColor
        listener.onColorChosen(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemColorBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position])
    }

    class Holder(private val binding: ItemColorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NamedColorListItem) {
            val namedColor = item.namedColor
            with(binding) {
                root.tag = namedColor
                colorView.setBackgroundColor(namedColor.value)
                colorNameTextView.text = namedColor.name
                selectedIndicatorImageView.visibility =
                    if (item.selected) View.VISIBLE else View.GONE
            }
        }
    }

    interface Listener {
        fun onColorChosen(namedColor: NamedColor)
    }
}