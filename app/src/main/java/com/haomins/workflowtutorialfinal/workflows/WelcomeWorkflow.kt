@file:OptIn(WorkflowUiExperimentalApi::class)

package com.haomins.workflowtutorialfinal.workflows

import com.haomins.workflowtutorialfinal.screens.WelcomeScreen
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.action
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

object WelcomeWorkflow :
    StatefulWorkflow<Unit, WelcomeWorkflow.State, WelcomeWorkflow.Output, WelcomeScreen>() {

    data class State(
        val username: TextController
    )

    data class Output(
        val username: String
    )

    override fun initialState(props: Unit, snapshot: Snapshot?): State {
        return State(
            /**
             * With [TextController] we no longer need to maintain a [String] for the username anymore.
             * @see [WelcomeScreen] for details.
             *
             * OLD tutorial: (hash: a69a23c)
             * @link https://github.com/square/workflow-kotlin/blob/main/samples/tutorial/tutorial-1-complete/src/main/java/workflow/tutorial/WelcomeWorkflow.kt
             */
            username = TextController(EMPTY_USERNAME)
        )
    }

    override fun render(
        renderProps: Unit,
        renderState: State,
        context: RenderContext
    ): WelcomeScreen {
        return WelcomeScreen(
            username = renderState.username,
            /**
             * With [TextController] we no longer need have a callback onUsernameChanged on for the
             * username EditText anymore.
             * @see [WelcomeScreen] for details.
             *
             * OLD tutorial: (hash: a69a23c)
             * @link https://github.com/square/workflow-kotlin/blob/main/samples/tutorial/tutorial-1-complete/src/main/java/workflow/tutorial/WelcomeWorkflow.kt
             */
            onLoginClicked = {
                context.actionSink.send(
                    onLogin()
                )
            }
        )
    }

    override fun snapshotState(state: State): Snapshot? {
        return null
    }

    internal fun onLogin(): WorkflowAction<Unit, State, Output> {
        return action {
            if (state.username.textValue.isNotEmpty())
                setOutput(Output(username = state.username.textValue))
        }
    }

    private const val EMPTY_USERNAME = ""
}