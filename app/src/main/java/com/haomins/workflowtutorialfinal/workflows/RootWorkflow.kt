package com.haomins.workflowtutorialfinal.workflows

import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.action
import com.squareup.workflow1.ui.Screen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.container.BackStackScreen
import com.squareup.workflow1.ui.container.toBackStackScreen

@OptIn(WorkflowUiExperimentalApi::class)
class RootWorkflow(
    private val welcomeWorkflow: WelcomeWorkflow = WelcomeWorkflow,
    private val todoListWorkflow: TodoListWorkflow = TodoListWorkflow
) : StatefulWorkflow<Unit, RootWorkflow.State, Nothing, BackStackScreen<Screen>>() {

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
    ): BackStackScreen<Screen> {

        val frames = mutableListOf<Screen>()
        val welcomeScreen = context.renderChild(
            child = welcomeWorkflow,
            props = Unit,
            handler = { login(it.username) }
        )

        frames.add(welcomeScreen)

        when (renderState) {
            is State.Welcome -> {
                // no-op since [WelcomeScreen] is always added
            }

            is State.Todo -> {
                val todoListScreen = context.renderChild(
                    child = todoListWorkflow,
                    props = TodoListWorkflow.Props(username = renderState.username),
                    handler = {
                        when (it) {
                            is TodoListWorkflow.Output.Back -> logout()
                        }
                    }
                )
                frames.add(todoListScreen)
            }
        }

        return frames.toBackStackScreen()
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