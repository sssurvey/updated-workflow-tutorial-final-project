package com.haomins.workflowtutorialfinal.workflows

import com.haomins.workflowtutorialfinal.model.TodoModel
import com.haomins.workflowtutorialfinal.screens.TodoEditScreen
import com.haomins.workflowtutorialfinal.screens.TodoListScreen
import com.squareup.workflow1.WorkflowOutput
import com.squareup.workflow1.testing.expectWorkflow
import com.squareup.workflow1.testing.testRender
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import org.junit.Assert.assertEquals
import org.junit.Test

class TodoWorkflowTest {

    @OptIn(WorkflowUiExperimentalApi::class)
    @Test
    fun `selecting todo`() {
        val todos = listOf(
            TodoModel("title", "note")
        )

        TodoWorkflow()
            .testRender(
                props = TodoWorkflow.Props("test"),
                initialState = TodoWorkflow.State(
                    todos = todos,
                    step = TodoWorkflow.State.Step.List
                )
            )
            .expectWorkflow(
                workflowType = TodoListWorkflow::class,
                rendering = TodoListScreen(
                    username = "test",
                    todoTitles = todos.map { it.title },
                    onTodoSelected = {},
                    onBack = {}
                ),
                output = WorkflowOutput(TodoListWorkflow.Output.SelectTodo(index = 0))
            )
            .render {
                assertEquals(1, it.size)
            }
            .verifyActionResult { state, _ ->
                assertEquals(
                    TodoWorkflow.State(
                        todos = todos,
                        step = TodoWorkflow.State.Step.Edit(index = 0)
                    ),
                    state
                )
            }

    }

    @OptIn(WorkflowUiExperimentalApi::class)
    @Test
    fun `save todo`() {
        val todos = listOf(
            TodoModel("title", "note")
        )

        TodoWorkflow()
            .testRender(
                props = TodoWorkflow.Props("test"),
                initialState = TodoWorkflow.State(
                    todos = todos,
                    step = TodoWorkflow.State.Step.Edit(0)
                )
            )
            .expectWorkflow(
                workflowType = TodoListWorkflow::class,
                rendering = TodoListScreen(
                    username = "",
                    todoTitles = listOf("title"),
                    onTodoSelected = {},
                    onBack = {}
                )
            )
            .expectWorkflow(
                workflowType = TodoEditWorkflow::class,
                rendering = TodoEditScreen(
                    todoTitle = TextController("title"),
                    todoContent = TextController("note"),
                    onSave = {},
                    onDiscard = {}
                ),
                output = WorkflowOutput(
                    TodoEditWorkflow.Output.Save(
                        TodoModel("new title", "new note")
                    )
                )
            )
            .render {
                assertEquals(2, it.size)
            }
            .verifyActionResult { state, _ ->
                assertEquals(
                    TodoWorkflow.State(
                        todos = listOf(TodoModel("new title", "new note")),
                        step = TodoWorkflow.State.Step.List
                    ),
                    state
                )
            }
    }

}