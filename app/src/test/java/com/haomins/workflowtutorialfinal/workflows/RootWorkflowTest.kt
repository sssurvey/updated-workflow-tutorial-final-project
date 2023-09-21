package com.haomins.workflowtutorialfinal.workflows

import com.haomins.workflowtutorialfinal.screens.TodoEditScreen
import com.haomins.workflowtutorialfinal.screens.TodoListScreen
import com.haomins.workflowtutorialfinal.screens.WelcomeScreen
import com.squareup.workflow1.WorkflowOutput
import com.squareup.workflow1.testing.expectWorkflow
import com.squareup.workflow1.testing.launchForTestingFromStartWith
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

    @OptIn(WorkflowUiExperimentalApi::class)
    @Test
    fun `render todo list`() {
        RootWorkflow()
            .testRender(initialState = RootWorkflow.State.Todo("test"), props = Unit)
            // you can render multiple workflows (BackStackScreens) by calling expectWorkflow multiple
            // times:
            // WelcomeWorkflow
            .expectWorkflow(
                workflowType = WelcomeWorkflow::class,
                rendering = WelcomeScreen(
                    TextController("test"),
                    onLoginClicked = {}
                )
            )
            // TodoWorkflow (backStack) with only TodoListWorkflow
            .expectWorkflow(
                workflowType = TodoWorkflow::class,
                rendering = listOf(
                    TodoListScreen("test", emptyList(), {}, {})
                )
            )
            .render {
                assertTrue(it.frames.size == 2)
                assertEquals("test", (it.frames.last() as TodoListScreen).username)
            }.verifyActionResult { state, _ ->
                assertEquals(RootWorkflow.State.Todo(username = "test"), state)
            }
    }

    @OptIn(WorkflowUiExperimentalApi::class)
    @Test
    fun `render todo list then user clicked back`() {
        RootWorkflow()
            .testRender(initialState = RootWorkflow.State.Todo("test"), props = Unit)
            .expectWorkflow(
                workflowType = WelcomeWorkflow::class,
                rendering = WelcomeScreen(
                    TextController("test"),
                    onLoginClicked = {}
                )
            )
            .expectWorkflow(
                workflowType = TodoWorkflow::class,
                rendering = listOf(
                    TodoListScreen("test", emptyList(), {}, {})
                ),
                output = WorkflowOutput(TodoWorkflow.Output.Back)
            )
            .render {
                assertTrue(it.frames.size == 2)
                assertTrue(it.frames.last() is TodoListScreen)
            }.verifyActionResult { state, _ ->
                assertEquals(
                    RootWorkflow.State.Welcome, state
                )
            }
    }

    @OptIn(WorkflowUiExperimentalApi::class)
    @Test
    fun `app flow`() {
        RootWorkflow().launchForTestingFromStartWith {

            // typed "test" as username, and clicked login
            awaitNextRendering().let {
                assertEquals(1, it.frames.size)
                val welcomeScreen = it.frames[0] as WelcomeScreen
                welcomeScreen.username.textValue = "test"
                welcomeScreen.onLoginClicked()
            }

            // check if todoListScreen added, and click first todoModel
            awaitNextRendering().let {
                assertEquals(2, it.frames.size)
                assertTrue(it.frames[0] is WelcomeScreen)
                assertTrue(it.frames[1] is TodoListScreen)
                val todoListScreen = it.frames[1] as TodoListScreen

                assertEquals(1, todoListScreen.todoTitles.size)

                todoListScreen.onTodoSelected.invoke(0)
            }

            // verify if todoEditScreen showed, and change "title" -> "new title", then save
            awaitNextRendering().let {
                assertEquals(3, it.frames.size)
                assertTrue(it.frames[0] is WelcomeScreen)
                assertTrue(it.frames[1] is TodoListScreen)
                assertTrue(it.frames[2] is TodoEditScreen)
                val todoEditScreen = it.frames[2] as TodoEditScreen
                todoEditScreen.todoTitle.textValue = "new title"
                todoEditScreen.onSave.invoke()
            }

            // returned to todoListScreen, check if todoList size is right and "new title" saved
            awaitNextRendering().let {
                assertEquals(2, it.frames.size)
                assertTrue(it.frames[0] is WelcomeScreen)
                assertTrue(it.frames[1] is TodoListScreen)
                val todoListScreen = it.frames[1] as TodoListScreen

                assertEquals(1, todoListScreen.todoTitles.size)
                assertEquals("new title", todoListScreen.todoTitles[0])
            }
        }
    }

}