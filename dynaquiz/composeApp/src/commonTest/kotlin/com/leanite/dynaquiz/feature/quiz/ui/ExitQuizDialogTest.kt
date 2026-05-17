package com.leanite.dynaquiz.feature.quiz.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextIsDisplayed
import com.leanite.dynaquiz.uitest.clickOnText
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class ExitQuizDialogTest : UiTest() {

    @Test
    fun `should render title body and both action buttons`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                ExitQuizDialog(onConfirm = {}, onDismiss = {})
            }
        }

        assertTextIsDisplayed("Opa! Vai mesmo?")
        assertTextIsDisplayed("Sair")
        assertTextIsDisplayed("Continuar")
    }

    @Test
    fun `should call onConfirm when user taps the exit action`() = runComposeUiTest {
        var confirmed = 0
        var dismissed = 0
        setContent {
            DynaquizTheme {
                ExitQuizDialog(onConfirm = { confirmed++ }, onDismiss = { dismissed++ })
            }
        }

        clickOnText("Sair")

        assertEquals(1, confirmed)
        assertEquals(0, dismissed)
    }

    @Test
    fun `should call onDismiss when user taps the continue action`() = runComposeUiTest {
        var confirmed = 0
        var dismissed = 0
        setContent {
            DynaquizTheme {
                ExitQuizDialog(onConfirm = { confirmed++ }, onDismiss = { dismissed++ })
            }
        }

        clickOnText("Continuar")

        assertEquals(0, confirmed)
        assertEquals(1, dismissed)
    }
}
