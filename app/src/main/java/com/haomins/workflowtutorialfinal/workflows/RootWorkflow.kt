package com.haomins.workflowtutorialfinal.workflows

import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.action
import com.squareup.workflow1.ui.Screen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

@OptIn(WorkflowUiExperimentalApi::class)
class RootWorkflow(
    private val welcomeWorkflow: WelcomeWorkflow = WelcomeWorkflow
): StatefulWorkflow<Unit, Unit, Nothing, Screen>() {

    override fun initialState(props: Unit, snapshot: Snapshot?) {
        return
    }

    override fun render(
        renderProps: Unit,
        renderState: Unit,
        context: RenderContext
    ): Screen {
        return context.renderChild(
            child = welcomeWorkflow,
            props = Unit,
            handler = {
                action {
                    //no op
                }
            }
        )
    }

    override fun snapshotState(state: Unit): Snapshot? {
        return null
    }

}