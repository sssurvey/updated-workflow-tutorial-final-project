@file:OptIn(WorkflowUiExperimentalApi::class)

package com.haomins.workflowtutorialfinal.workflows

import com.haomins.workflowtutorialfinal.model.TodoModel
import com.haomins.workflowtutorialfinal.screens.TodoListScreen
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.action
import com.squareup.workflow1.ui.Screen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

/**
 * This is almost the same to the original tutorial, no difference here aside from naming.
 */
class TodoListWorkflow(
    private val todoEditWorkflow: TodoEditWorkflow = TodoEditWorkflow
) :
    StatefulWorkflow<TodoListWorkflow.Props, TodoListWorkflow.State, TodoListWorkflow.Output, List<Screen>>() {

    data class Props(
        val username: String
    )

    data class State(
        val todos: List<TodoModel>,
        val step: Step
    ) {
        sealed class Step {
            data object List : Step()
            data class Edit(val index: Int) : Step()
        }
    }

    sealed class Output {
        data object Back : Output()
    }

    override fun initialState(props: Props, snapshot: Snapshot?): State {
        return State(
            todos = listOf(
                TodoModel(
                    title = "Take the cat for a walk",
                    note = "Cats really need their outside sunshine time. Don't forget to walk " +
                            "Charlie. Hamilton is less excited about the prospect."
                )
            ),
            step = State.Step.List
        )
    }

    override fun render(
        renderProps: Props,
        renderState: State,
        context: RenderContext
    ): List<Screen> {

        val frames = mutableListOf<Screen>()

        val todoListScreen = TodoListScreen(
            username = renderProps.username,
            todoTitles = renderState.todos.map { it.title },
            onTodoSelected = { context.actionSink.send(onSelect(selectedTodoItemIndex = it)) },
            onBack = { context.actionSink.send(onBack()) }
        )

        frames.add(todoListScreen)

        when (renderState.step) {
            is State.Step.List -> {}
            is State.Step.Edit -> {
                val todoEditScreen = context.renderChild(
                    child = todoEditWorkflow,
                    props = TodoEditWorkflow.Props(
                        initialTodo = renderState.todos[renderState.step.index]
                    ),
                    handler = {
                        when (it) {
                            is TodoEditWorkflow.Output.Save ->
                                onSaveEdit(renderState.step.index, it.todoModel)

                            is TodoEditWorkflow.Output.Discard ->
                                onDiscardEdit()
                        }
                    }
                )
                frames.add(todoEditScreen)
            }
        }

        return frames
    }

    override fun snapshotState(state: State): Snapshot? {
        return null
    }

    private fun onBack(): WorkflowAction<Props, State, Output> {
        return action {
            setOutput(Output.Back)
        }
    }

    private fun onSelect(selectedTodoItemIndex: Int): WorkflowAction<Props, State, Output> {
        return action {
            state = state.copy(
                step = State.Step.Edit(index = selectedTodoItemIndex)
            )
        }
    }

    private fun onSaveEdit(
        index: Int,
        todoModel: TodoModel
    ): WorkflowAction<Props, State, Output> {
        return action {
            val todoItems = state.todos.toMutableList()
            todoItems[index] = todoModel
            state = state.copy(step = State.Step.List, todos = todoItems)
        }
    }

    private fun onDiscardEdit(): WorkflowAction<Props, State, Output> {
        return action {
            state = state.copy(step = State.Step.List)
        }
    }
}