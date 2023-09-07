@file:OptIn(WorkflowUiExperimentalApi::class)

package com.haomins.workflowtutorialfinal.workflows

import com.haomins.workflowtutorialfinal.model.TodoModel
import com.haomins.workflowtutorialfinal.screens.TodoEditScreen
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.action
import com.squareup.workflow1.asWorker
import com.squareup.workflow1.runningWorker
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

object TodoEditWorkflow :
    StatefulWorkflow<TodoEditWorkflow.Props, TodoEditWorkflow.State, TodoEditWorkflow.Output, TodoEditScreen>() {

    data class Props(val initialTodo: TodoModel)
    data class State(val todoModel: TodoModel)
    sealed class Output {
        data class Save(
            val todoModel: TodoModel
        ) : Output()

        data object Discard : Output()
    }

    override fun initialState(props: Props, snapshot: Snapshot?): State {
        return State(
            props.initialTodo
        )
    }

    override fun render(
        renderProps: Props,
        renderState: State,
        context: RenderContext
    ): TodoEditScreen {

        val todoTitle = TextController(renderState.todoModel.title)
        val todoContent = TextController(renderState.todoModel.note)

        context.runningWorker(todoTitle.onTextChanged.asWorker()) {
            onTodoChanged(TodoModel(title = it, note = renderState.todoModel.note))
        }

        context.runningWorker(todoContent.onTextChanged.asWorker()) {
            onTodoChanged(TodoModel(title = renderState.todoModel.title, note = it))
        }

        return TodoEditScreen(
            todoTitle = todoTitle,
            todoContent = todoContent,
            onSave = { context.actionSink.send(onSave()) },
            onDiscard = { context.actionSink.send(onDiscard()) }
        )
    }

    override fun snapshotState(state: State): Snapshot? {
        return null
    }

    private fun onTodoChanged(todoModel: TodoModel): WorkflowAction<Props, State, Output> {
        return action {
            state = State(todoModel)
        }
    }

    private fun onSave(): WorkflowAction<Props, State, Output> {
        return action {
            setOutput(
                Output.Save(todoModel = state.todoModel)
            )
        }
    }

    private fun onDiscard(): WorkflowAction<Props, State, Output> {
        return action {
            setOutput(Output.Discard)
        }
    }

}