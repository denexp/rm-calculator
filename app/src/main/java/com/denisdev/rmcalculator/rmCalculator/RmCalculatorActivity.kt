package com.denisdev.rmcalculator.rmCalculator

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import com.denisdev.rmcalculator.base.BaseActivity
import com.denisdev.ui.theme.AppTheme
import com.denisdev.ui.theme.consumeSystemBarsPadding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RmCalculatorActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTheme {
                Scaffold {
                    Box(
                        Modifier.then(consumeSystemBarsPadding(it))
                    ) {
                        RMCalculator()
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RMCalculator() {
    val orientation: Int = LocalConfiguration.current.orientation
    val viewModel: RmCalculatorViewModel = hiltViewModel()
    val data by viewModel.data.collectAsState()

    val onEvent = { event: RmCalculatorEvent ->
        when (event) {
            is RmCalculatorEvent.UpdateRm -> viewModel.getRm(event.params)
        }
    }
    if (orientation == Configuration.ORIENTATION_LANDSCAPE)
        RMCalculatorLandscapeContent(data, onEvent)
    else
        RMCalculatorPortraitContent(data, onEvent)
}