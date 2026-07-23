package com.denisdev.rmcalculator.rmCalculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component2
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.usecases.rmcalculator.GetRm
import com.denisdev.domain.usecases.rmcalculator.GetRmImpl.Companion.CONSISTENT_RESULT_LIMIT
import com.denisdev.rmcalculator.R
import com.denisdev.rmcalculator.Utils.formatRound
import com.denisdev.ui.components.DropDown
import com.denisdev.ui.components.asResource
import com.denisdev.ui.components.take
import com.denisdev.ui.theme.AppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RMCalculatorPortraitContent(data: RmUiData, event: (RmCalculatorEvent) -> Unit = {}) {
    Column(modifier = Modifier.imePadding()
        .background(color = MaterialTheme.colorScheme.background)
        .padding(start = 12.dp, end = 12.dp, bottom = 8.dp, top = 4.dp)
        .fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            RMSection(data.rm, data.weightUnit)
        }

        InputFormSection(
            Modifier,
            data,
            { params ->
                event(RmCalculatorEvent.UpdateRm(params))
            }
        )
    }
}

@Composable
fun RMSection(
    rm: Float,
    weightUnit: WeightUnit
) {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = rm.formatRound(),
                style = TextStyle(fontSize = 70.sp, fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.testTag("RM")
            )
            Text(
                text = weightUnit.name, style = TextStyle(fontSize = 30.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        if (rm > 0)
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                PercentList(rm)
            }
    }
}

@Composable
fun PercentList(rm: Float) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            GeneratePercents(rm, 40..95, 5)
        }
        Column {
            GeneratePercents(rm, 10..35, 5)
            GeneratePercents(rm, 1..5, 1)
            PercentItem("0.5%", ((rm * 0.5f) / 100).formatRound())
        }
    }
}

fun getPercentList(rm: Float, range: IntRange, chunk: Int) =  (range).reversed().chunked(chunk).mapNotNull { it.firstOrNull() }
    .map { i ->
        ("$i   %".takeIf { i < 10 } ?: "$i %") to ((rm * i) / 100).formatRound()
    }

@Composable
private fun GeneratePercents(rm: Float, range: IntRange, chunk: Int) {
    getPercentList(rm, range, chunk).forEach {
        PercentItem(it.first, it.second)
    }
}

@Composable
private fun PercentItem(percent: String, nRm: String) {
    Row {
        Text(
            text = R.string.percent_and_weight.asResource(percent, nRm), style = TextStyle(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.testTag("PercentItem")
        )
    }
}

@Composable
fun InputFormSection(modifier: Modifier = Modifier,
                     data: RmUiData,
                     onParams: (GetRm.Params) -> Unit) {
    val (weight, onWeight) = rememberSaveable { mutableStateOf(data.weight) }
    val (reps, onReps) = rememberSaveable { mutableStateOf(data.reps) }
    val (weightUnit, onWeightUnit) = rememberSaveable { mutableStateOf(data.weightUnit) }
    val (author, onAuthor) = rememberSaveable { mutableStateOf(data.author) }
    val (autoFx, onAutoFx) = rememberSaveable { mutableStateOf(data.autoFx) }
    val (weightRequester, repsRequester) = remember { FocusRequester.createRefs() }

    LaunchedEffect(onAutoFx) {
        if (autoFx) onAuthor(data.author)
    }

    LaunchedEffect(onWeight, onReps, onAuthor, onWeightUnit, onAutoFx) {
        onParams(GetRm.Params(weight, reps, author, weightUnit, autoFx))
    }

    Column(modifier, verticalArrangement = Arrangement.Top) {
        MoreOptionsSection(weightUnit to onWeightUnit, data.authors, author to onAuthor, autoFx to onAutoFx, false)
        Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
            RmTextField(this, R.string.weight.asResource(), weight to onWeight, 0.6f,
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                weightRequester,
                "WeightTextField",
                weight.isNotEmpty().take {
                    ClearButton(onWeight, R.string.description_weight_clear_button.asResource())
                }
            )
            RmTextField(this, R.string.reps.asResource(), reps to onReps, 0.4f,
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                repsRequester,
                "RepsTextField",
                reps.isNotEmpty().take {
                    ClearButton(onReps, R.string.description_reps_clear_button.asResource())
                }
            )
        }
        if ((reps.toIntOrNull() ?: 0) > CONSISTENT_RESULT_LIMIT)
            Text(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
                text = R.string.less_reliable_results.asResource(CONSISTENT_RESULT_LIMIT + 1),
                style = TextStyle(fontSize = 14.sp),
                color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.End)
    }
    LaunchedEffect(Unit) { weightRequester.requestFocus() }
}

@Composable
private fun RmTextField(scope: RowScope, label: String, pair: Pair<String, (String) -> Unit>, weight: Float,
                        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
                        focusRequester: FocusRequester,
                        testTag: String,
                        trailingIcon: @Composable (() -> Unit)? = null) {
    with(scope) {
        OutlinedTextField(value = pair.first, label = {
            Text(
                text = label, style = TextStyle(fontSize = 20.sp),
                overflow = TextOverflow.Ellipsis, maxLines = 1
            )
        }, onValueChange = pair.second, modifier = Modifier
            .focusRequester(focusRequester)
            .weight(weight)
            .testTag(testTag),
            keyboardOptions = keyboardOptions,
            singleLine = true,
            trailingIcon = trailingIcon
        )
    }
}

@Composable
private fun ClearButton(
    action: (String) -> Unit,
    description: String
) {
    Icon(Icons.Rounded.Clear,
        description,
        modifier = Modifier
            .clickable {
                action(String())
            }
            .testTag("ClearButton"))
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MoreOptionsSection(weightUnit: Pair<WeightUnit, (WeightUnit) -> Unit>,
                               authors: List<Author>,
                               author: Pair<Author, (Author) -> Unit>,
                               autoFx: Pair<Boolean, (Boolean) -> Unit>,
                               expanded: Boolean) {
    var visible by rememberSaveable { mutableStateOf(expanded) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End.takeIf { !visible } ?: Arrangement.SpaceBetween
    ) {
        if (visible) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = R.string.auto_fx.asResource(), style = TextStyle(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.primary)
                Switch(checked = autoFx.first, onCheckedChange = autoFx.second, modifier = Modifier.testTag("AutoFxSwitch"))
            }
        }
        TextButton(modifier = Modifier.testTag("MoreOptionsButton"), onClick = { visible = !visible }, contentPadding = PaddingValues()) {
            Text(text = R.string.more_options.asResource(), style = TextStyle(fontSize = 20.sp))
            ExposedDropdownMenuDefaults.TrailingIcon(!visible)
        }
    }
    val authorNames by remember { derivedStateOf { authors.map { it.name } } }
    AnimatedVisibility(visible = visible) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.testTag("MoreOptionsDropDowns")) {
            DropDown(
                Modifier
                    .weight(0.7f)
                    .padding(vertical = 12.dp),
                !autoFx.first,
                TextStyle(fontSize = 20.sp),
                false,
                authorNames,
                author.first.name,
                { author.second(authors.first { author -> author.name == it }) },
                R.string.formula.asResource()
            )

            DropDown(
                Modifier
                    .padding(vertical = 12.dp)
                    .weight(0.3f),
                true,
                TextStyle(fontSize = 20.sp),
                false,
                WeightUnit.entries.map { it.name },
                weightUnit.first.name,
                { weightUnit.second(WeightUnit.valueOf(it)) },
                R.string.unit.asResource()
            )
        }
    }
}

@Preview(device = "spec:width=411dp,height=891dp", showSystemUi = true)
@Composable
fun RMCalculatorPortraitPreview() {
    AppTheme {
        RMCalculatorPortraitContent(RmUiData())
    }
}