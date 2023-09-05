package com.haomins.workflowtutorialfinal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haomins.workflowtutorialfinal.workflows.RootWorkflow
import com.squareup.workflow1.ui.Screen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.renderWorkflowIn
import kotlinx.coroutines.flow.StateFlow

@OptIn(WorkflowUiExperimentalApi::class)
class MainViewModel(savedState: SavedStateHandle) : ViewModel() {

    val renderings: StateFlow<Screen> by lazy {
        renderWorkflowIn(
            workflow = RootWorkflow(),
            scope = viewModelScope,
            savedStateHandle = savedState
        )
    }

}