@file:OptIn(WorkflowUiExperimentalApi::class)

package com.haomins.workflowtutorialfinal

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.haomins.workflowtutorialfinal.layoutrunners.WelcomeScreenRunner
import com.squareup.workflow1.ui.ViewRegistry
import com.squareup.workflow1.ui.WorkflowLayout
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
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
                 * [start] being deprecated, we now uses [take], to start the workflow in [MainActivity]
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
        private val viewRegistry = ViewRegistry(
            WelcomeScreenRunner
        )
    }
}