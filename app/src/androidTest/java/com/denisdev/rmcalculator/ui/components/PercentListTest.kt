package com.denisdev.rmcalculator.ui.components

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.denisdev.rmcalculator.Utils.formatRound
import com.denisdev.rmcalculator.assertContains
import com.denisdev.rmcalculator.base.AppTheme
import com.denisdev.rmcalculator.rmCalculator.PercentList
import com.denisdev.rmcalculator.rmCalculator.getPercentList
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PercentListTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val rm = 180f
    private val list = listOf(
        getPercentList(rm, 40..95, 5),
        getPercentList(rm, 10..35, 5),
        getPercentList(rm, 1..5, 1),
    ).flatten()
        .toMutableList().also { it.add("0.5%" to ((180f*0.5f)/100f).formatRound()) }
        .map {
        "${it.first} - ${it.second}"
    }

    @Before
    fun setUp() {
        composeTestRule.setContent {
            AppTheme {
                PercentList(rm)
            }
        }
    }

    @Test
    fun equalsList() {
        composeTestRule.onAllNodesWithTag("PercentItem")
            .assertCountEquals(list.count())
            .assertContains(list)
    }
}