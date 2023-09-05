package com.haomins.workflowtutorialfinal.workflows

import com.haomins.workflowtutorialfinal.screens.TodoListScreen
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow

object TodoListWorkflow : StatefulWorkflow<Unit, TodoListWorkflow.State, Nothing, TodoListScreen>() {

    data class State(
        val placeholder: String = ""
    )

    override fun initialState(props: Unit, snapshot: Snapshot?): State {
        return State("initial")
    }

    override fun render(
        renderProps: Unit,
        renderState: State,
        context: RenderContext
    ): TodoListScreen {
        return TodoListScreen(
            username = "",
            todoTitles = emptyList(),
            onTodoSelected = {},
            onBack = {}
        )
    }

    override fun snapshotState(state: State): Snapshot? {
        return null
    }

}