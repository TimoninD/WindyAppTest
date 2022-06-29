package com.windy.windyapptest.presentation.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.windy.windyapptest.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val composeView = ComposeView(baseContext)
        setContentView(composeView)
        subscribeState(composeView)
    }

    private fun subscribeState(composeView: ComposeView) {
        lifecycleScope.launch {
            viewModel.state.collect {
                composeView.setContent {
                    MainScreen(
                        state = it,
                        onStartClicked = {
                            viewModel.onStartClicked()
                        },
                        onTextChanged = {
                            viewModel.onTextChanged(it)
                        })
                }
            }
        }
    }
}