package com.haomins.workflowtutorialfinal.layoutrunners

import androidx.recyclerview.widget.LinearLayoutManager
import com.haomins.workflowtutorialfinal.adapters.TodoListAdapter
import com.haomins.workflowtutorialfinal.databinding.TodoListViewBinding
import com.haomins.workflowtutorialfinal.screens.TodoListScreen
import com.squareup.workflow1.ui.ScreenViewFactory
import com.squareup.workflow1.ui.ScreenViewRunner
import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.setBackHandler

@OptIn(WorkflowUiExperimentalApi::class)
class TodoListScreenRunner(
    private val todoListViewBinding: TodoListViewBinding
) : ScreenViewRunner<TodoListScreen> {

    private val layoutManager = LinearLayoutManager(todoListViewBinding.root.context)
    private val adapter = TodoListAdapter(dataSet = emptyList(), onItemClicked = {})

    init {
        todoListViewBinding.todoRecyclerView.let {
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
    }

    override fun showRendering(rendering: TodoListScreen, environment: ViewEnvironment) {
        with(todoListViewBinding) {
            root.setBackHandler { rendering.onBack.invoke() }
            usernameTitleTextView.text = rendering.username
            adapter.dataSet = rendering.todoTitles
            adapter.onItemClicked = rendering.onTodoSelected
        }
        adapter.notifyItemInserted(0)
    }

    companion object :
        ScreenViewFactory<TodoListScreen> by ScreenViewFactory.Companion.fromViewBinding(
            TodoListViewBinding::inflate,
            ::TodoListScreenRunner
        )
}