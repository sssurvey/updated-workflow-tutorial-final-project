package com.haomins.workflowtutorialfinal.workflows

import com.haomins.workflowtutorialfinal.model.TodoModel
import com.haomins.workflowtutorialfinal.screens.TodoListScreen
import com.squareup.workflow1.StatelessWorkflow
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.action

/**
 * Nothing special, this is almost the same as the original [TodoListWorkflow] from the original tutorial.
 */
object TodoListWorkflow :
    StatelessWorkflow<TodoListWorkflow.Props, TodoListWorkflow.Output, TodoListScreen>() {

    data class Props(
        val username: String,
        val todos: List<TodoModel>
    )

    sealed class Output {
        data object Back : Output()
        data class SelectTodo(val index: Int) : Output()
    }

    override fun render(
        renderProps: Props,
        context: RenderContext
    ): TodoListScreen {
        return TodoListScreen(
            username = renderProps.username,
            todoTitles = renderProps.todos.map { it.title },
            onTodoSelected = { context.actionSink.send(onSelect(selectedTodoItemIndex = it)) },
            onBack = { context.actionSink.send(onBack()) }
        )
    }

    private fun onBack(): WorkflowAction<Props, Nothing, Output> {
        return action {
            setOutput(Output.Back)
        }
    }

    private fun onSelect(selectedTodoItemIndex: Int): WorkflowAction<Props, Nothing, Output> {
        return action {
            setOutput(Output.SelectTodo(index = selectedTodoItemIndex))
        }
    }
}