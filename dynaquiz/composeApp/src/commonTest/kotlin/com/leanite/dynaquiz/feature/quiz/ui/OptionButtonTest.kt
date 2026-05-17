package com.leanite.dynaquiz.feature.quiz.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextIsDisplayed
import com.leanite.dynaquiz.uitest.assertTextIsNotEnabled
import com.leanite.dynaquiz.uitest.assertTextIsSelected
import com.leanite.dynaquiz.uitest.clickOnText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class OptionButtonTest : UiTest() {
    @Test
    fun `should render the option text`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    OptionButton(
                        text = "Brasília",
                        isSelected = false,
                        enabled = true,
                        onClick = {},
                    )
                }
            }

            assertTextIsDisplayed("Brasília")
        }

    @Test
    fun `should mark as selected when isSelected is true`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    OptionButton(
                        text = "Brasília",
                        isSelected = true,
                        enabled = true,
                        onClick = {},
                    )
                }
            }

            assertTextIsSelected("Brasília")
        }

    @Test
    fun `should call onClick when enabled and tapped`() =
        runComposeUiTest {
            var clicks = 0
            setContent {
                DynaquizTheme {
                    OptionButton(
                        text = "Brasília",
                        isSelected = false,
                        enabled = true,
                        onClick = { clicks++ },
                    )
                }
            }

            clickOnText("Brasília")

            assertEquals(1, clicks)
        }

    @Test
    fun `should not call onClick when disabled and tapped`() =
        runComposeUiTest {
            var clicks = 0
            setContent {
                DynaquizTheme {
                    OptionButton(
                        text = "Brasília",
                        isSelected = false,
                        enabled = false,
                        onClick = { clicks++ },
                    )
                }
            }

            assertTextIsNotEnabled("Brasília")
            runCatching { clickOnText("Brasília") }
            assertTrue(clicks == 0)
        }
}
