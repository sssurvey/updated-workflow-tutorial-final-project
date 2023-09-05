package com.haomins.workflowtutorialfinal.workflows

import com.haomins.workflowtutorialfinal.model.TodoModel
import com.haomins.workflowtutorialfinal.screens.TodoListScreen
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow

object TodoListWorkflow :
    StatefulWorkflow<Unit, TodoListWorkflow.State, Nothing, TodoListScreen>() {

    data class State(
        val todos: List<TodoModel>
    )

    override fun initialState(props: Unit, snapshot: Snapshot?): State {
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
        renderProps: Unit,
        renderState: State,
        context: RenderContext
    ): TodoListScreen {
        return TodoListScreen(
            username = "",
            todoTitles = renderState.todos.map { it.title },
            onTodoSelected = {},
            onBack = {}
        )
    }

    override fun snapshotState(state: State): Snapshot? {
        return null
    }

}