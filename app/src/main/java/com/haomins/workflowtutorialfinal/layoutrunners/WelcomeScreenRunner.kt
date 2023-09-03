package com.haomins.workflowtutorialfinal.layoutrunners

import android.widget.EditText
import com.haomins.workflowtutorialfinal.databinding.LayoutWelcomeScreenBinding
import com.haomins.workflowtutorialfinal.screens.WelcomeScreen
import com.squareup.workflow1.ui.LayoutRunner
import com.squareup.workflow1.ui.ScreenViewFactory
import com.squareup.workflow1.ui.ScreenViewRunner
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.control
import com.squareup.workflow1.ui.setTextChangedListener
import com.squareup.workflow1.ui.updateText

@OptIn(WorkflowUiExperimentalApi::class)
class WelcomeScreenRunner(
    private val layoutWelcomeScreenBinding: LayoutWelcomeScreenBinding
) : ScreenViewRunner<WelcomeScreen> {

    override fun showRendering(rendering: WelcomeScreen, environment: ViewEnvironment) {
        with(layoutWelcomeScreenBinding) {
            /**
             * Since we are using [TextController], this is how we call it onto the [EditText], to achieve
             * [updateText] and [setTextChangedListener].
             *
             * OLD tutorial: (hash: a69a23c)
             * @link https://github.com/square/workflow-kotlin/blob/main/samples/tutorial/tutorial-1-complete/src/main/java/workflow/tutorial/WelcomeLayoutRunner.kt
             */
            rendering.username.control(welcomePromptEditText)
            loginButton.setOnClickListener { rendering.onLoginClicked.invoke() }
        }
    }

    /**
     * This differs from the deprecated API, notice that instead by calling [LayoutRunner.bind] to
     * bind the view binding class with the runner, we now uses [ScreenViewFactory.Companion.fromViewBinding]
     *
     * OLD tutorial: (hash: a69a23c)
     * @link https://github.com/square/workflow-kotlin/blob/main/samples/tutorial/tutorial-1-complete/src/main/java/workflow/tutorial/WelcomeLayoutRunner.kt
     */
    companion object :
        ScreenViewFactory<WelcomeScreen> by ScreenViewFactory.Companion.fromViewBinding(
            LayoutWelcomeScreenBinding::inflate,
            ::WelcomeScreenRunner
        )

}