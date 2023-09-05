package com.haomins.workflowtutorialfinal.layoutrunners

import com.haomins.workflowtutorialfinal.databinding.TodoListViewBinding
import com.haomins.workflowtutorialfinal.screens.TodoListScreen
import com.squareup.workflow1.ui.ScreenViewFactory
import com.squareup.workflow1.ui.ScreenViewRunner
import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

@OptIn(WorkflowUiExperimentalApi::class)
class TodoListScreenRunner(
    private val todoListViewBinding: TodoListViewBinding
) : ScreenViewRunner<TodoListScreen> {

    override fun showRendering(rendering: TodoListScreen, environment: ViewEnvironment) {
        TODO("Not yet implemented")
    }

    companion object :
        ScreenViewFactory<TodoListScreen> by ScreenViewFactory.Companion.fromViewBinding(
            TodoListViewBinding::inflate,
            ::TodoListScreenRunner
        )
}