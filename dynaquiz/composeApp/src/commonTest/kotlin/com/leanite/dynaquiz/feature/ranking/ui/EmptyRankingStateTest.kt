package com.leanite.dynaquiz.feature.ranking.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.ranking.RankingTab
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextIsDisplayed
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class EmptyRankingStateTest : UiTest() {

    @Test
    fun `should render All tab empty copy when tab is All`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                EmptyRankingState(tab = RankingTab.All)
            }
        }

        assertTextIsDisplayed("Nenhum jogo registrado ainda")
    }

    @Test
    fun `should render Mine tab empty copy when tab is Mine`() = runComposeUiTest {
        setContent {
            DynaquizTheme {
                EmptyRankingState(tab = RankingTab.Mine)
            }
        }

        assertTextIsDisplayed("Você ainda não jogou")
    }
}
