package com.haomins.workflowtutorialfinal.workflows

import com.haomins.workflowtutorialfinal.model.TodoModel
import com.haomins.workflowtutorialfinal.screens.TodoListScreen
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.action

object TodoListWorkflow :
    StatefulWorkflow<TodoListWorkflow.Props, TodoListWorkflow.State, TodoListWorkflow.Output, TodoListScreen>() {

    data class Props(
        val username: String
    )

    data class State(
        val todos: List<TodoModel>
    )

    sealed class Output {
        data object Back : Output()
    }

    override fun initialState(props: Props, snapshot: Snapshot?): State {
        return State(
            todos = listOf(
                TodoModel(
                    title = "Take the cat for a walk",
                    note = "Cats really need their outside sunshine time. Don't forget to walk " +
                            "Charlie. Hamilton is less excited about the prospect."
                )
            )
        )
    }

    override fun render(
        renderProps: Props,
        renderState: State,
        context: RenderContext
    ): TodoListScreen {
        return TodoListScreen(
            username = renderProps.username,
            todoTitles = renderState.todos.map { it.title },
            onTodoSelected = {},
            onBack = { context.actionSink.send(onBack()) }
        )
    }

    override fun snapshotState(state: State): Snapshot? {
        return null
    }

    private fun onBack(): WorkflowAction<Props, State, Output> {
        return action {
            setOutput(Output.Back)
        }
    }
}