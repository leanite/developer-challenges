package com.leanite.dynaquiz.feature.difficulty

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextExists
import com.leanite.dynaquiz.uitest.assertTextIsNotEnabled
import com.leanite.dynaquiz.uitest.assertTextIsSelected
import com.leanite.dynaquiz.uitest.clickOnText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class DifficultyScreenTest : UiTest() {
    @Composable
    private fun DifficultyScreenContent(
        uiState: DifficultyUiState,
        onIntent: (DifficultyIntent) -> Unit,
    ) {
        DynaquizTheme {
            DifficultyScreen(uiState = uiState, onIntent = onIntent)
        }
    }

    @Test
    fun `should render all four challenge mode options`() =
        runComposeUiTest {
            setContent {
                DifficultyScreenContent(uiState = DifficultyUiState(), onIntent = {})
            }

            assertTextExists("Relaxado")
            assertTextExists("Fácil")
            assertTextExists("Médio")
            assertTextExists("Difícil")
        }

    @Test
    fun `should render the description of each mode`() =
        runComposeUiTest {
            setContent {
                DifficultyScreenContent(uiState = DifficultyUiState(), onIntent = {})
            }

            assertTextExists("Sem pressão, sem cronômetro")
            assertTextExists("30 segundos por pergunta")
            assertTextExists("20 segundos por pergunta")
            assertTextExists("10 segundos por pergunta")
        }

    @Test
    fun `should pre-select the mode coming from uiState`() =
        runComposeUiTest {
            setContent {
                DifficultyScreenContent(
                    uiState = DifficultyUiState(selectedMode = ChallengeMode.Timed.Hard),
                    onIntent = {},
                )
            }

            assertTextIsSelected("Difícil")
        }

    @Test
    fun `should emit ModeSelected when user taps a different mode option`() =
        runComposeUiTest {
            val captured = mutableListOf<DifficultyIntent>()
            setContent {
                DifficultyScreenContent(
                    uiState = DifficultyUiState(selectedMode = ChallengeMode.Timed.Easy),
                    onIntent = { captured += it },
                )
            }

            clickOnText("Difícil")

            assertEquals(1, captured.size)
            assertEquals(DifficultyIntent.ModeSelected(ChallengeMode.Timed.Hard), captured.single())
        }

    @Test
    fun `should emit ConfirmClicked when user taps the confirm button`() =
        runComposeUiTest {
            val captured = mutableListOf<DifficultyIntent>()
            setContent {
                DifficultyScreenContent(
                    uiState = DifficultyUiState(),
                    onIntent = { captured += it },
                )
            }

            clickOnText("CONFIRMAR")

            assertTrue(captured.contains(DifficultyIntent.ConfirmClicked))
        }

    @Test
    fun `should disable confirm button while isConfirming is true`() =
        runComposeUiTest {
            setContent {
                DifficultyScreenContent(
                    uiState = DifficultyUiState(isConfirming = true),
                    onIntent = {},
                )
            }

            assertTextIsNotEnabled("CONFIRMAR")
        }
}
