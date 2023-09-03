package com.haomins.workflowtutorialfinal.screens

import com.squareup.workflow1.ui.Screen
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

@OptIn(WorkflowUiExperimentalApi::class)
data class WelcomeScreen(
    val username: TextController,
    val onLoginClicked: () -> Unit
) : Screen