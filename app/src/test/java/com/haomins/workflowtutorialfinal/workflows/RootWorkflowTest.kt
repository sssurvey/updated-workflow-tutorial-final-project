package com.haomins.workflowtutorialfinal.workflows

import com.haomins.workflowtutorialfinal.screens.TodoListScreen
import com.haomins.workflowtutorialfinal.screens.WelcomeScreen
import com.squareup.workflow1.WorkflowOutput
import com.squareup.workflow1.testing.expectWorkflow
import com.squareup.workflow1.testing.testRender
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import org.junit.Assert.*
import org.junit.Test

class RootWorkflowTest {

    @OptIn(WorkflowUiExperimentalApi::class)
    @Test
    fun `welcome rendering`() {
        RootWorkflow()
            .testRender(initialState = RootWorkflow.State.Welcome, props = Unit)
            .expectWorkflow(
                workflowType = WelcomeWorkflow::class,
                rendering = WelcomeScreen(
                    TextController("test"),
                    onLoginClicked = {}
                )
            ).render {
                assertTrue(it.frames.size == 1)
                assertEquals("test", (it.frames.first() as WelcomeScreen).username.textValue)
            }.verifyActionResult { _, output ->
                assertNull(output)
            }
    }

    @OptIn(WorkflowUiExperimentalApi::class)
    @Test
    fun `login event`() {
        RootWorkflow()
            .testRender(initialState = RootWorkflow.State.Welcome, props = Unit)
            .expectWorkflow(
                workflowType = WelcomeWorkflow::class,
                rendering = WelcomeScreen(
                    TextController("test"),
                    onLoginClicked = {}
                ),
                output = WorkflowOutput(WelcomeWorkflow.Output("test"))
            ).render {
                assertTrue(it.frames.size == 1)
                assertEquals("test", (it.frames.first() as WelcomeScreen).username.textValue)
            }.verifyActionResult { state, _ ->
                assertEquals(RootWorkflow.State.Todo(username = "test"), state)
            }
    }

}