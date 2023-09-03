@file:OptIn(WorkflowUiExperimentalApi::class)

package com.haomins.workflowtutorialfinal.workflows

import android.util.Log
import com.haomins.workflowtutorialfinal.screens.WelcomeScreen
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.action
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

data class State(
    val username: TextController
)

object Output

object WelcomeWorkflow : StatefulWorkflow<Unit, State, Output, WelcomeScreen>() {

    override fun initialState(props: Unit, snapshot: Snapshot?): State {
        return State(
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
            onLoginClicked = {
                Log.d(
                    TAG,
                    "::onLoginClicked, the current username is ${renderState.username.textValue}"
                )
                action {
                    //NOOP
                }
            }
        )
    }

    override fun snapshotState(state: State): Snapshot? {
        return null
    }

    private const val EMPTY_USERNAME = ""
    private const val TAG = "WelcomeWorkflow"
}