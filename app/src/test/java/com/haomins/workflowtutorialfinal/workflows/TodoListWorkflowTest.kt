package com.haomins.workflowtutorialfinal.workflows

import com.haomins.workflowtutorialfinal.model.TodoModel
import com.squareup.workflow1.testing.testRender
import org.junit.Assert.assertEquals
import org.junit.Test

class TodoListWorkflowTest {

    val props = TodoListWorkflow.Props(
        username = "test",
        todos = listOf(
            TodoModel("test title", "test note")
        )
    )

    //TODO: maybe there is a better way to test this??
    @Test
    fun `onBack works`() {
        TodoListWorkflow.testRender(
            props = props
        ).render {
            assertEquals("test", it.username)
            assertEquals(1, it.todoTitles.size)
            assertEquals("test title", it.todoTitles.first())
            it.onBack.invoke()
        }.verifyActionResult { _, output ->
            assertEquals(TodoListWorkflow.Output.Back, output?.value)
        }
    }

    @Test
    fun `onSelect works`() {
        TodoListWorkflow.testRender(
            props = props
        ).render {
            it.onTodoSelected.invoke(0)
        }.verifyActionResult { _, output ->
            assertEquals(TodoListWorkflow.Output.SelectTodo(0), output?.value)
        }
    }

}