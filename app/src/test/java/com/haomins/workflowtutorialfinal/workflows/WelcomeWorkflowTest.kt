package com.haomins.workflowtutorialfinal.workflows

import com.squareup.workflow1.ActionApplied
import com.squareup.workflow1.applyTo
import com.squareup.workflow1.testing.testRender
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import org.junit.Assert.*
import org.junit.Test

class WelcomeWorkflowTest {

    /**
     * This is different from the tutorial since I don't really have any [WelcomeWorkflow.State]
     * that contains a value that changes when user type in anything, because it is backed by [TextController]
     *
     * So I decided to skip `username updates` test
     * //TODO: investigate this, can we test [TextController]???
     */
    @OptIn(WorkflowUiExperimentalApi::class)
    @Test
    fun `login works`() {

        val textController = TextController("")
        val startState = WelcomeWorkflow.State(textController)

        textController.textValue = "test"
        assertTrue(startState.username.textValue == "test")

        val action = WelcomeWorkflow.onLogin()
        val (state, output) = action.applyTo(state = startState, props = Unit)

        /**
         * Not I am calling `output.output` to verify action? This will be explained later.
         */
        assertNotNull(output.output)
        assertEquals("test", state.username.textValue)
    }

    @OptIn(WorkflowUiExperimentalApi::class)
    @Test
    fun `login does nothing when name is empty`() {

        val textController = TextController("")
        val startState = WelcomeWorkflow.State(textController)

        val action = WelcomeWorkflow.onLogin()
        val (state, output) = action.applyTo(state = startState, props = Unit)

        /**
         * Notice here I have to call Output.output? Since output is a [ActionApplied] class, it tells
         * me if the action.applyTo()'s result, not the actual output from the [WelcomeWorkflow].
         *
         * In the old tutorial it is implying that the `output` here is the [WelcomeWorkflow.Output],
         * which is incorrect. It is actually [ActionApplied] (`output: ActionApplied`)
         *
         * NOTE: The tutorial is not up to date:
         * @link: https://github.com/square/workflow-kotlin/blob/main/samples/tutorial/Tutorial5.md
         * In the tutorial the sample code is checking `assertNull(output)`. However, in the sample
         * code solution included in the project, it has been updated.
         *
         * Note: Link to the tutorial solution from the workflow lib:
         * @link: https://github.com/square/workflow-kotlin/blob/main/samples/tutorial/tutorial-final/src/test/java/workflow/tutorial/WelcomeWorkflowTest.kt
         */
        assertNull(output.output)
        assertEquals("", state.username.textValue)
    }

    @OptIn(WorkflowUiExperimentalApi::class)
    @Test
    fun `rendering initial`() {
        WelcomeWorkflow.testRender(
            props = Unit
        ).render {
            assertEquals("", it.username.textValue)
            it.onLoginClicked.invoke()
        }.verifyActionResult { _, output ->
            assertNull(output)
        }
    }


    @OptIn(WorkflowUiExperimentalApi::class)
    @Test
    fun `render login`() {
        WelcomeWorkflow.testRender(
            props = Unit,
            initialState = WelcomeWorkflow.State(TextController("test"))
        ).render {
            it.onLoginClicked.invoke()
        }.verifyActionResult { _, output ->
            assertEquals("test", output?.value?.username)
        }
    }

}