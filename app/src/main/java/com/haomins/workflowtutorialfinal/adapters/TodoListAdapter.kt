package com.haomins.workflowtutorialfinal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.haomins.workflowtutorialfinal.databinding.TodoItemViewBinding

class TodoListAdapter(
    var dataSet: List<String>,
    var onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<TodoListAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemBinding = TodoItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CustomViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(dataSet[position])
        holder.itemView.setOnClickListener {
            onItemClicked(position)
        }
    }

    inner class CustomViewHolder(
        binding: TodoItemViewBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val todoTitleTextView: AppCompatTextView

        init {
            todoTitleTextView = binding.todoItemTitleTextView
        }

        fun bind(todoTitle: String) {
            todoTitleTextView.text = todoTitle
        }
    }
}