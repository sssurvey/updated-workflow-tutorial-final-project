package com.haomins.workflowtutorialfinal.workflows

import com.haomins.workflowtutorialfinal.model.TodoModel
import com.squareup.workflow1.applyTo
import com.squareup.workflow1.testing.testRender
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(WorkflowUiExperimentalApi::class)
class TodoEditWorkflowTest {

    private val testTitleTextController = TextController("Title")
    private val testNoteTextController = TextController("Note")
    private val startState = TodoEditWorkflow.State(
        todoModel = TodoModel(
            title = "Title",
            note = "Note"
        ),
        titleTextController = testTitleTextController,
        noteTextController = testNoteTextController
    )

    @Test
    fun `todo is saved`() {
        val testTodoModel = TodoModel("test", "test note")

        // props does not matter in this case
        val props = TodoEditWorkflow
            .Props(initialTodo = TodoModel("", ""))

        val (_, output) = TodoEditWorkflow.testOnSave(testTodoModel)
            .applyTo(props, startState)

        assertNotNull(output.output)
        assertEquals(TodoEditWorkflow.Output.Save(todoModel = testTodoModel), output.output!!.value)
    }

    @Test
    fun `todo is discarded`() {
        // props does not matter in this case
        val props = TodoEditWorkflow
            .Props(initialTodo = TodoModel("", ""))

        val (_, output) = TodoEditWorkflow.testOnDiscard()
            .applyTo(props, startState)

        assertNotNull(output.output)
        assertTrue(output.output!!.value is TodoEditWorkflow.Output.Discard)
    }

    @Test
    fun `changed props updated local state`() {
        val testTodoModel = TodoModel("test", "test note")

        val props = TodoEditWorkflow
            .Props(initialTodo = TodoModel("init", "init note"))

        val state = TodoEditWorkflow.initialState(props, null)

        assertEquals("init", state.todoModel.title)
        assertEquals("init note", state.todoModel.note)

        val updatedState = TodoEditWorkflow.onPropsChanged(
            old = props,
            new = TodoEditWorkflow.Props(initialTodo = testTodoModel),
            state = state
        )

        assertEquals("test", updatedState.todoModel.title)
        assertEquals("test note", updatedState.todoModel.note)
    }

    @Test
    fun `on props changed with diff new props`() {
        val props = TodoEditWorkflow
            .Props(initialTodo = TodoModel("init", "init note"))
        val newProps = TodoEditWorkflow
            .Props(initialTodo = TodoModel("new init", "new init note"))
        val state = TodoEditWorkflow.initialState(props, null)
        val newState = TodoEditWorkflow.onPropsChanged(props, newProps, state)
        assertEquals(newProps.initialTodo, newState.todoModel)
    }

    @Test
    fun `on props changed with same props`() {
        val props = TodoEditWorkflow
            .Props(initialTodo = TodoModel("init", "init note"))
        val newProps = TodoEditWorkflow
            .Props(initialTodo = TodoModel("init", "init note"))
        val state = TodoEditWorkflow.initialState(props, null)
        val newState = TodoEditWorkflow.onPropsChanged(props, newProps, state)
        assertEquals(state, newState)
    }

    @Test
    fun `render initial`() {
        val props = TodoEditWorkflow
            .Props(initialTodo = TodoModel("init", "init note"))

        TodoEditWorkflow.testRender(
            props = props
        ).render {
            assertEquals("init", it.todoTitle.textValue)
            assertEquals("init note", it.todoContent.textValue)
        }
    }

    @Test
    fun `render save`() {
        val props = TodoEditWorkflow
            .Props(initialTodo = TodoModel("init", "init note"))
        val state = TodoEditWorkflow.initialState(props, null)

        TodoEditWorkflow.testRender(
            props = props,
            initialState = state
        ).render {
            it.onSave.invoke()
        }.verifyActionResult { _, output ->
            assertNotNull(output)
            assertEquals(
                props.initialTodo,
                (output?.value as? TodoEditWorkflow.Output.Save)?.todoModel
            )
        }
    }

    @Test
    fun `render discard`() {
        val props = TodoEditWorkflow
            .Props(initialTodo = TodoModel("init", "init note"))
        val state = TodoEditWorkflow.initialState(props, null)

        TodoEditWorkflow.testRender(
            props = props,
            initialState = state
        ).render {
            it.onDiscard.invoke()
        }.verifyActionResult { _, output ->
            assertNotNull(output)
            assertTrue(output?.value is TodoEditWorkflow.Output.Discard)
        }
    }
}