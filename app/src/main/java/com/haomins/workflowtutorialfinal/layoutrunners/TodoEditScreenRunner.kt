package com.haomins.workflowtutorialfinal.layoutrunners

import com.haomins.workflowtutorialfinal.databinding.TodoEditViewBinding
import com.haomins.workflowtutorialfinal.screens.TodoEditScreen
import com.squareup.workflow1.ui.ScreenViewFactory
import com.squareup.workflow1.ui.ScreenViewRunner
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.control
import com.squareup.workflow1.ui.setBackHandler

@OptIn(WorkflowUiExperimentalApi::class)
class TodoEditScreenRunner(
    private val todoEditViewBinding: TodoEditViewBinding
) : ScreenViewRunner<TodoEditScreen> {

    /**
     * This is the same as the old tutorial aside from we are now using [TextController].
     *
     * OLD tutorial: (hash: a69a23c)
     * @link: https://github.com/square/workflow-kotlin/blob/main/samples/tutorial/tutorial-3-complete/src/main/java/workflow/tutorial/TodoEditLayoutRunner.kt
     */
    override fun showRendering(rendering: TodoEditScreen, environment: ViewEnvironment) {
        with(todoEditViewBinding) {
            rendering.todoTitle.control(todoTitleEditText)
            rendering.todoContent.control(todoContentEditText)
            confirmButton.setOnClickListener { rendering.onSave.invoke() }
            discardButton.setOnClickListener { rendering.onDiscard.invoke() }
            todoEditViewBinding.root.setBackHandler { rendering.onDiscard.invoke() }
        }
    }

    companion object :
        ScreenViewFactory<TodoEditScreen> by ScreenViewFactory.Companion.fromViewBinding(
            TodoEditViewBinding::inflate,
            ::TodoEditScreenRunner
        )
}