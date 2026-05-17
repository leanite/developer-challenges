package com.leanite.dynaquiz.core.ui.common

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextIsDisplayed
import com.leanite.dynaquiz.uitest.assertTextIsNotEnabled
import com.leanite.dynaquiz.uitest.clickOnText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class GeneralActionButtonTest : UiTest() {
    @Test
    fun `should render the provided text`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    GeneralActionButton(text = "SAVE", onClick = {})
                }
            }

            assertTextIsDisplayed("SAVE")
        }

    @Test
    fun `should call onClick when tapped while enabled`() =
        runComposeUiTest {
            var clicks = 0
            setContent {
                DynaquizTheme {
                    GeneralActionButton(text = "SAVE", onClick = { clicks++ })
                }
            }

            clickOnText("SAVE")

            assertEquals(1, clicks)
        }

    @Test
    fun `should not call onClick when disabled and tapped`() =
        runComposeUiTest {
            var clicks = 0
            setContent {
                DynaquizTheme {
                    GeneralActionButton(text = "SAVE", onClick = { clicks++ }, enabled = false)
                }
            }

            assertTextIsNotEnabled("SAVE")
            runCatching { clickOnText("SAVE") }
            assertTrue(clicks == 0)
        }
}
