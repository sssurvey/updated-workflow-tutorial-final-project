@file:OptIn(WorkflowUiExperimentalApi::class)

package com.haomins.workflowtutorialfinal.workflows

import com.haomins.workflowtutorialfinal.model.TodoModel
import com.haomins.workflowtutorialfinal.screens.TodoEditScreen
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.action
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import org.jetbrains.annotations.TestOnly

object TodoEditWorkflow :
    StatefulWorkflow<TodoEditWorkflow.Props, TodoEditWorkflow.State, TodoEditWorkflow.Output, TodoEditScreen>() {

    data class Props(val initialTodo: TodoModel)
    data class State(
        val todoModel: TodoModel,
        /**
         * Based on some of the code I see in the workflow-kotlin lib, I think it is best to keep the
         * [TextController]s in the [State] that belongs to the workflow. Once we received the [Props]
         * we can initialized the [TextController] in [State]. As [TextController] can be see as a
         * [State] also.
         */
        val titleTextController: TextController,
        val noteTextController: TextController
    )

    sealed class Output {
        data class Save(
            val todoModel: TodoModel
        ) : Output()

        data object Discard : Output()
    }

    override fun initialState(props: Props, snapshot: Snapshot?): State {
        return State(
            props.initialTodo,
            titleTextController = TextController(initialValue = props.initialTodo.title),
            noteTextController = TextController(initialValue = props.initialTodo.note),
        )
    }

    override fun onPropsChanged(old: Props, new: Props, state: State): State {
        return if (old.initialTodo != new.initialTodo)
            state.copy(
                todoModel = new.initialTodo,
                titleTextController = TextController(initialValue = new.initialTodo.title),
                noteTextController = TextController(initialValue = new.initialTodo.note),
            ) else state
    }

    override fun render(
        renderProps: Props,
        renderState: State,
        context: RenderContext
    ): TodoEditScreen {
        return TodoEditScreen(
            todoTitle = renderState.titleTextController,
            todoContent = renderState.noteTextController,
            onSave = {
                context.actionSink.send(
                    onSave(
                        TodoModel(
                            title = renderState.titleTextController.textValue,
                            note = renderState.noteTextController.textValue
                        )
                    )
                )
            },
            onDiscard = { context.actionSink.send(onDiscard()) }
        )
    }

    override fun snapshotState(state: State): Snapshot? {
        return null
    }

    private fun onSave(todoModel: TodoModel): WorkflowAction<Props, State, Output> {
        return action {
            setOutput(
                Output.Save(todoModel = todoModel)
            )
        }
    }

    private fun onDiscard(): WorkflowAction<Props, State, Output> {
        return action {
            setOutput(Output.Discard)
        }
    }

    @TestOnly
    internal fun testOnSave(todoModel: TodoModel): WorkflowAction<Props, State, Output> {
        return onSave(todoModel)
    }

    @TestOnly
    internal fun testOnDiscard(): WorkflowAction<Props, State, Output> {
        return onDiscard()
    }

}