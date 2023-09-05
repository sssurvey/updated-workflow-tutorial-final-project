package com.haomins.workflowtutorialfinal.workflows

import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.action
import com.squareup.workflow1.ui.Screen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

@OptIn(WorkflowUiExperimentalApi::class)
class RootWorkflow(
    private val welcomeWorkflow: WelcomeWorkflow = WelcomeWorkflow,
    private val todoListWorkflow: TodoListWorkflow = TodoListWorkflow
) : StatefulWorkflow<Unit, RootWorkflow.State, Nothing, Screen>() {

    sealed class State {
        data object Welcome : State()
        data class Todo(val username: String) : State()
    }

    override fun initialState(props: Unit, snapshot: Snapshot?): State {
        return State.Welcome
    }

    override fun render(
        renderProps: Unit,
        renderState: State,
        context: RenderContext
    ): Screen {
        return when (renderState) {
            is State.Welcome -> {
                val welcomeScreen = context.renderChild(
                    child = welcomeWorkflow,
                    props = Unit,
                    handler = { login(it.username) }
                )
                welcomeScreen
            }

            is State.Todo -> {
                val todoListScreen = context.renderChild(
                    child = todoListWorkflow,
                    props = TodoListWorkflow.Props(username = renderState.username),
                    handler = { logout() }
                )
                todoListScreen
            }
        }

    }

    override fun snapshotState(state: State): Snapshot? {
        return null
    }

    private fun login(username: String): WorkflowAction<Unit, State, Nothing> {
        return action {
            state = State.Todo(username)
        }
    }

    private fun logout(): WorkflowAction<Unit, State, Nothing> {
        return action {
            state = State.Welcome
        }
    }

}