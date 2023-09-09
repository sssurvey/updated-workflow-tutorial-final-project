@file:OptIn(WorkflowUiExperimentalApi::class)

package com.haomins.workflowtutorialfinal.workflows

import com.haomins.workflowtutorialfinal.model.TodoModel
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.action
import com.squareup.workflow1.ui.Screen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

/**
 * Pretty much the same as original [TodoWorkflow]. Aside from DI, and [List] of [Screen] as rendering.
 */
class TodoWorkflow(
    private val todoListWorkflow: TodoListWorkflow = TodoListWorkflow,
    private val todoEditWorkflow: TodoEditWorkflow = TodoEditWorkflow
) :
    StatefulWorkflow<TodoWorkflow.Props, TodoWorkflow.State, TodoWorkflow.Output, List<Screen>>() {

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
        val todoListWorkflow = context.renderChild(
            child = todoListWorkflow,
            props = TodoListWorkflow.Props(
                username = renderProps.username,
                todos = renderState.todos
            ),
            handler = {
                when (it) {
                    is TodoListWorkflow.Output.SelectTodo -> {
                        editTodo(it.index)
                    }

                    is TodoListWorkflow.Output.Back -> {
                        onBack()
                    }
                }
            }
        )

        frames.add(todoListWorkflow)

        when (renderState.step) {
            is State.Step.List -> {
                // no op since we always add [TodoListScreen]
            }

            is State.Step.Edit -> {
                val todoEditWorkflow = context.renderChild(
                    child = todoEditWorkflow,
                    props = TodoEditWorkflow.Props(
                        initialTodo = renderState.todos[renderState.step.index]
                    ),
                    handler = {
                        when (it) {
                            is TodoEditWorkflow.Output.Save -> {
                                onSaveEdit(
                                    index = renderState.step.index,
                                    todoModel = it.todoModel
                                )
                            }

                            is TodoEditWorkflow.Output.Discard -> {
                                onDiscardEdit()
                            }
                        }
                    }
                )
                frames.add(todoEditWorkflow)
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

    private fun editTodo(index: Int): WorkflowAction<Props, State, Output> {
        return action {
            state = state.copy(step = State.Step.Edit(index = index))
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