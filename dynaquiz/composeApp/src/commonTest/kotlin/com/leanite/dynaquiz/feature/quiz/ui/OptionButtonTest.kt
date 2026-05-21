package com.leanite.dynaquiz.feature.quiz.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextIsDisplayed
import com.leanite.dynaquiz.uitest.assertTextIsNotEnabled
import com.leanite.dynaquiz.uitest.assertTextIsSelected
import com.leanite.dynaquiz.uitest.clickOnText
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class OptionButtonTest : UiTest() {
    @Composable
    private fun OptionButtonContent(
        text: String = "Brasília",
        isSelected: Boolean = false,
        enabled: Boolean = true,
        onClick: () -> Unit = {},
    ) {
        OptionButton(
            text = text,
            isSelected = isSelected,
            enabled = enabled,
            onClick = onClick,
        )
    }

    @Test
    fun `should render the option text`() =
        runComposeUiTest {
            setContent {
                OptionButtonContent()
            }

            assertTextIsDisplayed("Brasília")
        }

    @Test
    fun `should mark as selected when isSelected is true`() =
        runComposeUiTest {
            setContent {
                OptionButtonContent(isSelected = true)
            }

            assertTextIsSelected("Brasília")
        }

    @Test
    fun `should call onClick when enabled and tapped`() =
        runComposeUiTest {
            var clicks = 0

            setContent {
                OptionButtonContent(
                    onClick = { clicks++ },
                )
            }

            clickOnText("Brasília")

            assertEquals(1, clicks)
        }

    @Test
    fun `should not call onClick when disabled and tapped`() =
        runComposeUiTest {
            var clicks = 0

            setContent {
                OptionButtonContent(
                    enabled = false,
                    onClick = { clicks++ },
                )
            }

            assertTextIsNotEnabled("Brasília")
            runCatching { clickOnText("Brasília") }

            assertEquals(clicks, 0)
        }
}
