@file:OptIn(WorkflowUiExperimentalApi::class)

package com.haomins.workflowtutorialfinal

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.haomins.workflowtutorialfinal.layoutrunners.TodoEditScreenRunner
import com.haomins.workflowtutorialfinal.layoutrunners.TodoListScreenRunner
import com.haomins.workflowtutorialfinal.layoutrunners.WelcomeScreenRunner
import com.haomins.workflowtutorialfinal.workflows.RootWorkflow
import com.squareup.workflow1.ui.ViewRegistry
import com.squareup.workflow1.ui.WorkflowLayout
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.container.BackStackContainer
import com.squareup.workflow1.ui.container.BackStackScreen
import com.squareup.workflow1.ui.container.withRegistry
import com.squareup.workflow1.ui.start
import kotlinx.coroutines.flow.map

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            WorkflowLayout(this).apply {
                /**
                 * [start] being deprecated, we now uses [WorkflowLayout.take], to start the workflow in [MainActivity]
                 *
                 * OLD tutorial: (hash: a69a23c)
                 * @link: https://github.com/square/workflow-kotlin/blob/main/samples/tutorial/tutorial-1-complete/src/main/java/workflow/tutorial/TutorialActivity.kt
                 */
                take(
                    lifecycle,
                    mainViewModel.renderings.map { it.withRegistry(viewRegistry) }
                )
            }
        )
    }

    companion object {
        /**
         * Notice here is different from the original tutorial 2. We are using a [BackStackContainer],
         * so that we can have it work with the [BackStackScreen] as the renderings for the
         * [RootWorkflow]. However, with the new API, the [BackStackContainer] support is builtin,
         * and because of that, we do not need to include [BackStackContainer] here.
         *
         * OLD tutorial: (hash: a69a23c)
         * @link: https://github.com/square/workflow-kotlin/blob/main/samples/tutorial/tutorial-2-complete/src/main/java/workflow/tutorial/TutorialActivity.kt
         */
        private val viewRegistry = ViewRegistry(
            WelcomeScreenRunner,
            TodoListScreenRunner,
            TodoEditScreenRunner
        )
    }
}