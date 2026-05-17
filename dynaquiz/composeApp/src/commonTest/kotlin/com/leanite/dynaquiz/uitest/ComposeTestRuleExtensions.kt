package com.leanite.dynaquiz.uitest

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput

@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.clickOnText(text: String) {
    onNodeWithText(text, substring = false, ignoreCase = false).performClick()
}

@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.assertTextExists(text: String) {
    onNodeWithText(text, substring = false, ignoreCase = false).assertExists()
}

@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.assertTextContainingExists(substring: String) {
    onNodeWithText(substring, substring = true).assertExists()
}

@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.clickOnTextContaining(substring: String) {
    onNodeWithText(substring, substring = true, ignoreCase = false).performClick()
}

@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.clickOnTag(tag: String) {
    onNodeWithTag(tag).performClick()
}

@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.clickOnContentDescription(description: String) {
    onNodeWithContentDescription(description).performClick()
}

@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.typeOnField(label: String, text: String) {
    onNodeWithText(label).performTextInput(text)
}

@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.typeOnTag(tag: String, text: String) {
    onNodeWithTag(tag).performTextInput(text)
}

@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.clearField(label: String) {
    onNodeWithText(label).performTextClearance()
}

@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.assertTextIsDisplayed(text: String) {
    onNodeWithText(text, substring = false, ignoreCase = false).assertIsDisplayed()
}

@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.assertTextContainingIsDisplayed(substring: String) {
    onNodeWithText(substring, substring = true).assertIsDisplayed()
}

@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.assertTagIsDisplayed(tag: String) {
    onNodeWithTag(tag).assertIsDisplayed()
}

@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.assertTagIsEnabled(tag: String) {
    onNodeWithTag(tag).assertIsEnabled()
}

@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.assertTagIsNotEnabled(tag: String) {
    onNodeWithTag(tag).assertIsNotEnabled()
}

@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.assertTextIsEnabled(text: String) {
    onNodeWithText(text).assertIsEnabled()
}

@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.assertTextIsNotEnabled(text: String) {
    onNodeWithText(text).assertIsNotEnabled()
}

@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.assertTextIsSelected(text: String) {
    onNodeWithText(text).assertIsSelected()
}
