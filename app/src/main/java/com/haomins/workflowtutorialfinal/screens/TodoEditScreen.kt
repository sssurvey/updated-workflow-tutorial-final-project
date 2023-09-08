package com.haomins.workflowtutorialfinal.screens

import com.squareup.workflow1.ui.Screen
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

@OptIn(WorkflowUiExperimentalApi::class)
data class TodoEditScreen(
    val todoTitle: TextController,
    val todoContent: TextController,
    val onSave: () -> Unit,
    val onDiscard: () -> Unit
) : Screen