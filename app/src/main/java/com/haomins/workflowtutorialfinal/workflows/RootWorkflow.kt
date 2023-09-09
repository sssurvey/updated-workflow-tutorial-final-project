package com.haomins.workflowtutorialfinal.workflows

import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.action
import com.squareup.workflow1.ui.Screen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.container.BackStackScreen
import com.squareup.workflow1.ui.container.toBackStackScreen

/**
 * Here I decided to use manual DI to compose the [RootWorkflow]. IMO it is clearer and easier to read.
 * So this is different from the original tutorial, but this is simply a preference in this case. And
 * because of the manual DI, this class cannot be a static class anymore.
 *
 * Another thing worth to point out is the renderings of [StatefulWorkflow] generic type is now a [BackStackScreen]
 * of [Screen] instead of a [BackStackScreen] of [Any], since I extended all screen renderings with [Screen].
 */
@OptIn(WorkflowUiExperimentalApi::class)
class RootWorkflow(
    private val welcomeWorkflow: WelcomeWorkflow = WelcomeWorkflow,
    private val todoWorkflow: TodoWorkflow = TodoWorkflow()
) : StatefulWorkflow<Unit, RootWorkflow.State, Nothing, BackStackScreen<Screen>>() {

    sealed class State {

        // data object is added in later versions of kotlin to have better toString() etc support
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
                    child = todoWorkflow,
                    props = TodoWorkflow.Props(username = renderState.username),
                    handler = {
                        when (it) {
                            is TodoWorkflow.Output.Back -> logout()
                        }
                    }
                )
                frames.addAll(todoListScreen)
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