package com.haomins.workflowtutorialfinal.screens

import com.squareup.workflow1.ui.Screen
import com.squareup.workflow1.ui.ScreenViewRunner
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

/**
 * This should contain all data to display in the UI.
 *
 * It should also contain callbacks for any UI events, for example:
 * `val onButtonTapped: () -> Unit`.
 *
 * Note: Notice that I am have extend the [Screen] interface, this is different compared to the original
 * tutorial, I need to extend the [Screen] here so that I can later use [TodoListScreen] with [ScreenViewRunner].
 */
@OptIn(WorkflowUiExperimentalApi::class)
data class TodoListScreen(
    val username: String,
    val todoTitles: List<String>,
    val onTodoSelected: (Int) -> Unit,
    val onBack: () -> Unit
) : Screen