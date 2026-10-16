package com.denisdev.rmcalculator.rmCalculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.denisdev.ui.theme.AppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RMCalculatorLandscapeContent(data: RmUiData, event: (RmCalculatorEvent) -> Unit = {}) {
    Row(modifier = Modifier
        .background(color = MaterialTheme.colorScheme.background)
        .padding(start = 12.dp, end = 12.dp, bottom = 8.dp, top = 4.dp)
        .fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(modifier = Modifier.weight(1f)) {
            RMSection(data.rm, data.weightUnit)
        }

        InputFormSection(
            Modifier
                .weight(1f)
                .fillMaxHeight(),
            data,
            { params ->
                event(RmCalculatorEvent.UpdateRm(params))
            }
        )
    }
}

@Preview(device = "spec:width=411dp,height=891dp,orientation=landscape", showSystemUi = true)
@Composable
fun RMCalculatorPreviewLandscape() {
    AppTheme {
        RMCalculatorLandscapeContent(RmUiData())
    }
}