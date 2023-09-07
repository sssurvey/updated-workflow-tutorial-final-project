package com.haomins.workflowtutorialfinal.screens

import com.squareup.workflow1.ui.Screen
import com.squareup.workflow1.ui.ScreenViewRunner
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.setTextChangedListener
import com.squareup.workflow1.ui.updateText

@OptIn(WorkflowUiExperimentalApi::class)
data class WelcomeScreen(

    /**
     * Here is also different from the tutorial that is currently online, notice that we are using
     * [TextController] here instead of a [String] for username, the [TextController] allows us to
     * achieve the [updateText] and [setTextChangedListener] with less code. Which is a reason why
     * we do not have the `onUsernameChanged` method in [WelcomeScreen] anymore.
     *
     * Note: Notice that I am have extend the [Screen] interface, this is different compared to the original
     * tutorial, I need to extend the [Screen] here so that I can later use [TodoListScreen] with [ScreenViewRunner].
     *
     * OLD tutorial: (hash: a69a23c)
     * @link https://github.com/square/workflow-kotlin/blob/main/samples/tutorial/tutorial-1-complete/src/main/java/workflow/tutorial/WelcomeScreen.kt
     */
    val username: TextController,
    val onLoginClicked: () -> Unit
) : Screen