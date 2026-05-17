package com.leanite.dynaquiz.feature.quiz.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextIsDisplayed
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class CountdownDisplayTest : UiTest() {
    @Test
    fun `should render the seconds remaining number`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    CountdownDisplay(secondsRemaining = 3)
                }
            }

            assertTextIsDisplayed("3")
        }

    @Test
    fun `should render zero when secondsRemaining is zero`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    CountdownDisplay(secondsRemaining = 0)
                }
            }

            assertTextIsDisplayed("0")
        }
}
