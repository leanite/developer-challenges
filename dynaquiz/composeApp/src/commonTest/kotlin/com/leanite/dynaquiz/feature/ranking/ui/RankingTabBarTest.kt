package com.leanite.dynaquiz.feature.ranking.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.ranking.RankingTab
import com.leanite.dynaquiz.uitest.UiTest
import com.leanite.dynaquiz.uitest.assertTextIsDisplayed
import com.leanite.dynaquiz.uitest.assertTextIsSelected
import com.leanite.dynaquiz.uitest.clickOnText
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class RankingTabBarTest : UiTest() {
    @Test
    fun `should render both All and Mine tab labels`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    RankingTabBar(selectedTab = RankingTab.All, onTabSelected = {})
                }
            }

            assertTextIsDisplayed("Todos")
            assertTextIsDisplayed("Meus jogos")
        }

    @Test
    fun `should mark All tab as selected when selectedTab is All`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    RankingTabBar(selectedTab = RankingTab.All, onTabSelected = {})
                }
            }

            assertTextIsSelected("Todos")
        }

    @Test
    fun `should mark Mine tab as selected when selectedTab is Mine`() =
        runComposeUiTest {
            setContent {
                DynaquizTheme {
                    RankingTabBar(selectedTab = RankingTab.Mine, onTabSelected = {})
                }
            }

            assertTextIsSelected("Meus jogos")
        }

    @Test
    fun `should emit onTabSelected with All when user taps the All tab`() =
        runComposeUiTest {
            val captured = mutableListOf<RankingTab>()
            setContent {
                DynaquizTheme {
                    RankingTabBar(selectedTab = RankingTab.Mine, onTabSelected = { captured += it })
                }
            }

            clickOnText("Todos")

            assertEquals(listOf(RankingTab.All), captured)
        }

    @Test
    fun `should emit onTabSelected with Mine when user taps the Mine tab`() =
        runComposeUiTest {
            val captured = mutableListOf<RankingTab>()
            setContent {
                DynaquizTheme {
                    RankingTabBar(selectedTab = RankingTab.All, onTabSelected = { captured += it })
                }
            }

            clickOnText("Meus jogos")

            assertEquals(listOf(RankingTab.Mine), captured)
        }
}
